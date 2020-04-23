import { Editor, Element } from "@kogito-tooling/core-api";
import * as AppFormer from "appformer-js";
import React from "react";
import { LinearRegressionViewer } from "./LinearRegressionViewer";

type Props = {

}

type State = {
  viewer: Element;
}

class KogitoLinearRegressionViewer extends Editor {

  private static COMPONENT_ID: string = "KogitoLinearRegressionViewer";

  private content: string = "";
  private wrapper: Wrapper = new Wrapper();

  constructor() {
    super(KogitoLinearRegressionViewer.COMPONENT_ID);
    this.af_isReact = true;
    this.af_componentTitle = "PMML Linear Regression Model viewer";
  }

  public setContent(path: string, content: string): Promise<void> {
    this.content = content;
    this.wrapper.setContent(content);
    return Promise.resolve();
  }

  public getContent(): Promise<string> {
    return Promise.resolve(this.content);
  }

  public isDirty(): boolean {
    return false;
  }

  public getPreview(): Promise<string | undefined> {
    return Promise.resolve(undefined);
  }

  public af_componentRoot(): Element {
    return this.wrapper.render();
  }

}

class Wrapper extends React.Component<Props, State>  {

  constructor() {
    super({});
    this.state = { viewer: <><LinearRegressionViewer /></> };
  }

  setContent(content: string): void {
    this.setState({ viewer: <><LinearRegressionViewer xml={content} /></> });
  }

  render(): Element {
    return this.state.viewer;
  }

}

AppFormer.register(new KogitoLinearRegressionViewer());

export { KogitoLinearRegressionViewer };

