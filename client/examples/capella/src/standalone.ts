import {
  TYPES,
  /*LocalModelSource,*/

  RequestModelAction,
  WebSocketDiagramServer
} from '../../../src'

import createContainer from './di.config'
/*
import graph from './schema'
*/

const WebSocket = require("reconnecting-websocket")
/*
const runCapellaArchitectureDiagram = () => {
  const container = createContainer(false, 'sprotty')
  const modelSource = container.get<LocalModelSource>(TYPES.ModelSource)
  modelSource.setModel(graph)
}

export default runCapellaArchitectureDiagram
*/

export default function runCapellaWsArchitectureDiagram() {
    const websocket = new WebSocket('ws://' + window.location.host + '/diagram')
    setup(websocket)
}

export function setup(websocket: WebSocket) {
  const container = createContainer(true, 'sprotty')

  // Connect to the diagram server
  const diagramServer = container.get<WebSocketDiagramServer>(TYPES.ModelSource)
  diagramServer.listen(websocket)
  websocket.addEventListener('open', event => {
    // Run
    function run() {
      const resourceId = 'architecture-diagram'
      diagramServer.clientId = resourceId + '_capella'
      diagramServer.handle(new RequestModelAction({ resourceId }))
    }
    run()
  })
}
