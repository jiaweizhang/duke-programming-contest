const React = require('react');
const Util = require('./Util');

export class Nux extends React.Component{
  constructor(props) {
    super(props);
  }

  closeNux() {
    let timesSeen = localStorage.getItem('accountInfoNux');
    if (timesSeen) {
      let newTimesSeen = parseInt(timesSeen) + 1;
      localStorage.setItem('accountInfoNux', newTimesSeen);
    } else {
      localStorage.setItem('accountInfoNux', 1);
    }
    Util.fadeElement(this.props.id + '-nux', () => {
      document.getElementById(this.props.id + '-nux').classList.add('hidden');
    });
  }

  render() {
    return (
      <div id={this.props.id + '-nux'} className="nux-container" style={{
        left: this.props.position.left ? this.props.position.left : '',
        right: this.props.position.right ? this.props.position.right : '',
        top: this.props.position.top ? this.props.position.top : '',
        bottom: this.props.position.bottom ? this.props.position.bottom : ''
      }}>
        <div className="nux-arrow"></div>
        <div className="nux">
          <div className="nux-message">
            {this.props.message}
          </div>
        </div>
        <div onClick={this.closeNux.bind(this)} className="close-nux">
          <img src="assets/close.png" width="20" height="20"/>
        </div>
      </div>
    );
  }
}

module.exports = Nux;
