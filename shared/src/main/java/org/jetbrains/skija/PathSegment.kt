package org.jetbrains.skija

import java.util.*

data class PathSegment(
    val verb: PathVerb? = null,
    val p0: Point? = null,
    val p1: Point? = null,
    val p2: Point? = null,
    val p3: Point? = null,
    val conicWeight: Float = 0f,
    val closeLine: Boolean = false,
    val closedContour: Boolean = false,
) {


    constructor() : this(
        verb = PathVerb.DONE,
        p0 = null,
        p1 = null,
        p2 = null,
        p3 = null,
        conicWeight = 0f,
        closeLine = false,
        closedContour = false
    )

    constructor(verbOrdinal: Int, x0: Float, y0: Float, isClosedContour: Boolean) : this(
        verb = PathVerb.values()[verbOrdinal],
        p0 = Point(x0, y0),
        p1 = null,
        p2 = null,
        p3 = null,
        conicWeight = 0f,
        closeLine = false,
        closedContour = isClosedContour
    ) {
        require(verbOrdinal == PathVerb.MOVE.ordinal || verbOrdinal == PathVerb.CLOSE.ordinal) {
            "Expected MOVE or CLOSE, got ${PathVerb.values()[verbOrdinal]}"
        }
    }

    constructor(x0: Float, y0: Float, x1: Float, y1: Float, isCloseLine: Boolean, isClosedContour: Boolean) : this(
        verb = PathVerb.LINE,
        p0 = Point(x0, y0),
        p1 = Point(x1, y1),
        p2 = null,
        p3 = null,
        conicWeight = 0f,
        closeLine = isCloseLine,
        closedContour = isClosedContour
    )

    constructor(x0: Float, y0: Float, x1: Float, y1: Float, x2: Float, y2: Float, isClosedContour: Boolean) : this(
        verb = PathVerb.QUAD,
        p0 = Point(x0, y0),
        p1 = Point(x1, y1),
        p2 = Point(x2, y2),
        p3 = null,
        conicWeight = 0f,
        closeLine = false,
        closedContour = isClosedContour
    )

    constructor(
        x0: Float,
        y0: Float,
        x1: Float,
        y1: Float,
        x2: Float,
        y2: Float,
        conicWeight: Float,
        isClosedContour: Boolean
    ) : this(
        verb = PathVerb.CONIC,
        p0 = Point(x0, y0),
        p1 = Point(x1, y1),
        p2 = Point(x2, y2),
        p3 = null,
        conicWeight = conicWeight,
        closeLine = false,
        closedContour = isClosedContour
    )

    constructor(
        x0: Float,
        y0: Float,
        x1: Float,
        y1: Float,
        x2: Float,
        y2: Float,
        x3: Float,
        y3: Float,
        isClosedContour: Boolean
    ) : this(
        verb = PathVerb.CUBIC,
        p0 = Point(x0, y0),
        p1 = Point(x1, y1),
        p2 = Point(x2, y2),
        p3 = Point(x3, y3),
        conicWeight = 0f,
        closeLine = false,
        closedContour = isClosedContour
    )

    override fun toString(): String {
        return "Segment(" +
                "verb=" + verb +
                (if (verb != PathVerb.DONE) ", p0=$p0" else "") +
                (if (verb == PathVerb.LINE || verb == PathVerb.QUAD || verb == PathVerb.CONIC || verb == PathVerb.CUBIC) ", p1=$p1" else "") +
                (if (verb == PathVerb.QUAD || verb == PathVerb.CONIC || verb == PathVerb.CUBIC) ", p2=$p2" else "") +
                (if (verb == PathVerb.CUBIC) ", p3=$p3" else "") +
                (if (verb == PathVerb.CONIC) ", conicWeight=$conicWeight" else "") +
                (if (verb == PathVerb.LINE) ", closeLine=$closeLine" else "") +
                (if (verb != PathVerb.DONE) ", closedContour=$closedContour" else "") +
                ")"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val segment = other as PathSegment
        return verb == segment.verb &&
                (if (verb != PathVerb.DONE) p0 == segment.p0 else true) &&
                (if (verb == PathVerb.LINE || verb == PathVerb.QUAD || verb == PathVerb.CONIC || verb == PathVerb.CUBIC) p1 == segment.p1 else true) &&
                (if (verb == PathVerb.QUAD || verb == PathVerb.CONIC || verb == PathVerb.CUBIC) p2 == segment.p2 else true) &&
                (if (verb == PathVerb.CUBIC) p3 == segment.p3 else true) &&
                (if (verb == PathVerb.CONIC) java.lang.Float.compare(
                    segment.conicWeight,
                    conicWeight
                ) == 0 else true) &&
                (if (verb == PathVerb.LINE) closeLine == segment.closeLine else true) &&
                if (verb != PathVerb.DONE) closedContour == segment.closedContour else true
    }

    override fun hashCode(): Int {
        return when (verb) {
            PathVerb.DONE -> Objects.hash(verb)
            PathVerb.MOVE -> Objects.hash(verb, p0, closedContour)
            PathVerb.LINE -> Objects.hash(verb, p0, p1, closeLine, closedContour)
            PathVerb.QUAD -> Objects.hash(verb, p0, p1, p2, closedContour)
            PathVerb.CONIC -> Objects.hash(verb, p0, p1, p2, conicWeight, closedContour)
            PathVerb.CUBIC -> Objects.hash(verb, p0, p1, p2, p3, closedContour)
            else -> throw RuntimeException("Unreachable")
        }
    }
}