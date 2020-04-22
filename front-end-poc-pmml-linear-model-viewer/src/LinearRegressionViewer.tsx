import React, { useState } from "react";
import ReactDOM from "react-dom";
import { document as PMMLDocument } from "./generated/www.dmg.org/PMML-4_4";
import { LinearRegressionViewAdaptor } from "./LinearRegressionViewAdaptor";
import { withCXML as unmarshal } from "./unmarshall/unmarshaller";
import { Spinner } from '@patternfly/react-core';

type Props = {
  xml: string
}

const LinearRegressionViewer = (props: Props) => {

  const [viewer, setViewer] = useState(<><Spinner size="xl" /></>);

  unmarshal(props.xml).then((doc: PMMLDocument) => {
    if (doc.PMML.RegressionModel !== undefined) {
      if (doc.PMML.RegressionModel[0] !== undefined) {
        setViewer(<><LinearRegressionViewAdaptor dictionary={doc.PMML.DataDictionary} model={doc.PMML.RegressionModel} /></>);
      }
    };
  });

  return viewer;
}

export { LinearRegressionViewer };

