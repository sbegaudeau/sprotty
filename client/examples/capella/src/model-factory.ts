import {
  getBasicType,
  HtmlRoot,
  HtmlRootSchema,
  SChildElement,
  SGraphFactory,
  SModelElementSchema,
  SModelRootSchema,
  SModelRoot,
  SParentElement,
  PreRenderedElement,
  PreRenderedElementSchema
} from "../../../src"

export class CapellaArchitectureDiagramFactory extends SGraphFactory {
  createElement(schema: SModelElementSchema, parent?: SParentElement): SChildElement {
    if (this.isPreRenderedSchema(schema)) {
      return this.initializeChild(new PreRenderedElement(), schema, parent)
    } else {
      return super.createElement(schema, parent)
    }
  }

  createRoot(schema: SModelRootSchema): SModelRoot {
    if (this.isHtmlRootSchema(schema)) {
      return this.initializeRoot(new HtmlRoot(), schema)
    } else {
      return super.createRoot(schema)
    }
  }

  isHtmlRootSchema(schema: SModelRootSchema): schema is HtmlRootSchema {
    return getBasicType(schema) === 'html'
  }

  isPreRenderedSchema(schema: SModelElementSchema): schema is PreRenderedElementSchema {
    return getBasicType(schema) === 'pre-rendered'
  }
}