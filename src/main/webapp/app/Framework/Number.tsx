import * as React from "react";
import "./Number.css";
interface Props {
  children?: number | string | boolean;

  shorten?: boolean | number;
  isCurrency?: boolean;
  isPercent?: boolean;
  maxDecimals?: number;
  loading?: boolean;
  style?: React.CSSProperties;
  className?: string;
}

export default class Number extends React.Component<Props> {
  static defaultProps: Partial<Props> = {
    isCurrency: true,
  };

  _renderContent() {
    const number = this.props.children;
    if(number > 9999999){
      return <p style={{fontSize: 30}}>∞</p>;
    }else{
      return number;
    }
  }



  render() {
    return (
      <span
        lang="num"
        className={this.props.className}
        style={this.props.style}
      >
         {this._renderContent()}
      </span>
    );
  }
}
