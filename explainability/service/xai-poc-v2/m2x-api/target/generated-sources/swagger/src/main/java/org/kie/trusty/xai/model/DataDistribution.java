/*
 * Model to Explainability communication
 * Model to Explainability communication protocol
 *
 * OpenAPI spec version: 1.0.0
 * Contact: tteofili@redhat.com
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package org.kie.trusty.xai.model;

import java.util.Objects;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.kie.trusty.xai.model.FeatureDistribution;

/**
 * DataDistribution
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2020-05-04T14:59:31.604+02:00")
public class DataDistribution {
  @SerializedName("featureDistributions")
  private List<FeatureDistribution> featureDistributions = null;

  public DataDistribution featureDistributions(List<FeatureDistribution> featureDistributions) {
    this.featureDistributions = featureDistributions;
    return this;
  }

  public DataDistribution addFeatureDistributionsItem(FeatureDistribution featureDistributionsItem) {
    if (this.featureDistributions == null) {
      this.featureDistributions = new ArrayList<FeatureDistribution>();
    }
    this.featureDistributions.add(featureDistributionsItem);
    return this;
  }

   /**
   * Get featureDistributions
   * @return featureDistributions
  **/
  @ApiModelProperty(value = "")
  public List<FeatureDistribution> getFeatureDistributions() {
    return featureDistributions;
  }

  public void setFeatureDistributions(List<FeatureDistribution> featureDistributions) {
    this.featureDistributions = featureDistributions;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DataDistribution dataDistribution = (DataDistribution) o;
    return Objects.equals(this.featureDistributions, dataDistribution.featureDistributions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(featureDistributions);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DataDistribution {\n");
    
    sb.append("    featureDistributions: ").append(toIndentedString(featureDistributions)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}
