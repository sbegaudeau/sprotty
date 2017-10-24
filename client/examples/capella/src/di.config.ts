import {
  Container,
  ContainerModule
} from "inversify"

import {
  boundsModule,
  defaultModule,
  exportModule,
  hoverModule,
  moveModule,
  selectModule,
  undoRedoModule,
  viewportModule,
  overrideViewerOptions,

  ConsoleLogger,
  LogLevel,
  LocalModelSource,
  ViewRegistry,
  WebSocketDiagramServer,

  TYPES,

  HtmlRootView,
  PreRenderedView,
  SCompartmentView,
  SLabelView,
  SGraphView
} from '../../../src'

import {
  CapellaComponentNodeView,
  CapellaEdgeView,
  CapellaInputPortView,
  CapellaServiceNodeView,
  CapellaOutputPortView
} from './views'

import { CapellaArchitectureDiagramFactory } from './model-factory'

import { capellaPopupModelFactory } from './popup'

const capellaArchitectureDiagramModule = new ContainerModule((bind, unbind, isBound, rebind) => {
  rebind(TYPES.ILogger).to(ConsoleLogger).inSingletonScope()
  rebind(TYPES.LogLevel).toConstantValue(LogLevel.log)
  rebind(TYPES.IModelFactory).to(CapellaArchitectureDiagramFactory).inSingletonScope()
  bind(TYPES.PopupModelFactory).toConstantValue(capellaPopupModelFactory)
})

const createContainer = (useWebsocket: boolean, containerId: string) => {
  const container = new Container()
  container.load(defaultModule,
    selectModule,
    moveModule,
    boundsModule,
    undoRedoModule,
    viewportModule,
    hoverModule,
    exportModule,
    capellaArchitectureDiagramModule)

  if (useWebsocket) {
    container.bind(TYPES.ModelSource).to(WebSocketDiagramServer).inSingletonScope()
  } else {
    container.bind(TYPES.ModelSource).to(LocalModelSource).inSingletonScope()
  }
  overrideViewerOptions(container, {
    needsClientLayout: true,
    needsServerLayout: true,
    baseDiv: containerId,
    hiddenDiv: containerId + '-hidden'
  })

  const viewRegistry = container.get<ViewRegistry>(TYPES.ViewRegistry)
  viewRegistry.register('graph', SGraphView)
  viewRegistry.register('node:capellacomponent', CapellaComponentNodeView)
  viewRegistry.register('node:capellaservice', CapellaServiceNodeView)
  viewRegistry.register('port:capellainputport', CapellaInputPortView)
  viewRegistry.register('port:capellaoutputport', CapellaOutputPortView)
  viewRegistry.register('edge:straight', CapellaEdgeView)
  viewRegistry.register('label:heading', SLabelView)
  viewRegistry.register('label:text', SLabelView)
  viewRegistry.register('comp:main', SCompartmentView)
  viewRegistry.register('html', HtmlRootView)
  viewRegistry.register('pre-rendered', PreRenderedView)

  return container
}

export default createContainer