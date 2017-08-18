
import {
  SModelElementSchema,
  SModelRootSchema,
  RequestPopupModelAction,
  PreRenderedElementSchema
} from '../../../src'

export const capellaPopupModelFactory = (request: RequestPopupModelAction, element?: SModelElementSchema): SModelRootSchema | undefined => {
  if (element !== undefined && element.type === 'node:class') {
    return {
      type: 'html',
      id: 'popup',
      children: [
        <PreRenderedElementSchema> {
          type: 'pre-rendered',
          id: 'popup-title',
          code: `<div class="popup-title">Class ${element.id === 'node0' ? 'Foo' : 'Bar'}</div>`
        },
        <PreRenderedElementSchema> {
          type: 'pre-rendered',
          id: 'popup-body',
          code: '<div class="popup-body">Hello World</div>'
        }
      ]
    }
  }
  return undefined
}
