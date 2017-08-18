import { PolylineEdgeView, RenderingContext, SEdge, SNode, SPort, RectangularNodeView, Point, toDegrees, angle } from "../../../src"
import { VNode } from "snabbdom/vnode"
import * as snabbdom from "snabbdom-jsx"

const JSX = {createElement: snabbdom.svg}

export class CapellaComponentNodeView extends RectangularNodeView {
    render(node: SNode, context: RenderingContext): VNode {
        return <g class-component={true}>
            <rect class-component={true} class-selected={node.selected} class-mouseover={node.hoverFeedback}
                  x={0} y={0}
                  rx="5" ry="5"
                  width={Math.max(0, node.bounds.width)} height={Math.max(0, node.bounds.height)} />
            {context.renderChildren(node)}
        </g>
    }
}

export class CapellaServiceNodeView extends RectangularNodeView {
    render(node: SNode, context: RenderingContext): VNode {
        return (
            <g class-service={true}>
                <rect class-service={true} class-selected={node.selected} class-mouseover={node.hoverFeedback}
                    x={0} y ={0}
                    rx="5" ry="5"
                    width={Math.max(0, node.bounds.width)} height={Math.max(0, node.bounds.height)}/>
                {context.renderChildren(node)}
            </g>
        )
    }
}

export class CapellaInputPortView extends RectangularNodeView {
    render(node: SPort, context: RenderingContext): VNode {
        return (
            <g class-inputport={true}>
                <rect class-inputport={true} class-selected={node.selected} class-mouseover={node.hoverFeedback}
                    x={0} y ={0}
                    width={Math.max(5, node.bounds.width)} height={Math.max(5, node.bounds.height)}/>
                {context.renderChildren(node)}
            </g>
        )
    }
}

export class CapellaOutputPortView extends RectangularNodeView {
    render(node: SPort, context: RenderingContext): VNode {
        return (
            <g class-outputport={true}>
                <rect class-outputport={true} class-selected={node.selected} class-mouseover={node.hoverFeedback}
                    x={0} y ={0}
                    width={Math.max(5, node.bounds.width)} height={Math.max(5, node.bounds.height)}/>
                {context.renderChildren(node)}
            </g>
        )
    }
}

export class CapellaEdgeView extends PolylineEdgeView {
    protected renderAdditionals(edge: SEdge, segments: Point[], context: RenderingContext): VNode[] {
        const p1 = segments[segments.length - 2]
        const p2 = segments[segments.length - 1]
        return [
            <path class-edge={true} class-arrow={true} d="M 0,0 L 10,-4 L 10,4 Z"
                  transform={`rotate(${toDegrees(angle(p2, p1))} ${p2.x} ${p2.y}) translate(${p2.x} ${p2.y})`}/>
        ]
    }
}