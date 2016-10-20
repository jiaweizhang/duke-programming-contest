const React = require('react');
const TITLE_HEIGHT = 100;
const CONTAINER_WIDTH = 500;
const CONTAINER_HEIGHT = 420;

export class Registration extends React.Component{
  constructor(props) {
    super(props);
    console.log('init');
    this.state = {
      error: false
    }
  }

  submitLoginInfo(e) {
    e.preventDefault();
    console.log('submitted');
    const emailField = document.getElementById('email-login-field');
    const passwordField = document.getElementById('password-login-field');
    const email = emailField.value;
    const password = passwordField.value;
    console.log(email, ',', password);
    if (email.length == 0 || password.length == 0) {
      console.log('error!');
    }
  }

  render() {
    const width = this.props.dimensions.width - 160;
    const height = this.props.dimensions.height - 100;

    return (
      <div className="registration-tab">
        <div
          className="registration card"
          style={{
            width: width,
            height: height
          }}>
          <div className="tab-title">
            Registration
          </div>
          <div
            className="registration-form-container"
            style={{
              left: (width - CONTAINER_WIDTH)/2 + 'px',
              top: (height - CONTAINER_HEIGHT - TITLE_HEIGHT)/2 + TITLE_HEIGHT + 'px',
              width: CONTAINER_WIDTH + 'px',
              height: CONTAINER_HEIGHT + 'px',
              borderRadius: '10px',
              boxShadow: '0 0 2px black'
            }}>
            <div style={{
              width: '300px',
              position: 'absolute',
              left: (CONTAINER_WIDTH - 320)/2 + 'px',
              top: '20px'
            }}>
              <div className="registration-form-login unselectable">
                Registered User?
              </div>
              <form onSubmit={(e) => this.submitLoginInfo(e)}>
                <input type="text" id="email-login-field" className="text-field" placeholder="Email"/>
                <input
                  type="password"
                  id="password-login-field"
                  className="text-field"
                  placeholder="Password"
                  autoComplete="new-password"/>
                <input type="submit" style={{display: 'none'}}/>
              </form>
              <div className="link-button login" onClick={this.submitLoginInfo.bind(this)}>Login</div>
              <div style={{
                width: '200px',
                marginLeft: '50px',
                marginTop: '40px'
              }}>
                <hr className="registration-divider"/>
                <div style={{
                  backgroundColor: 'white',
                  textAlign: 'center',
                  width: '50px',
                  position: 'relative',
                  top: '-20px',
                  left: '75px'
                }}>
                  OR
                </div>
              </div>
              <div
                className="link-button register registration-form-sign-up">
                <i className="fa fa-pencil-square-o" aria-hidden="true"></i>
                &nbsp; Sign Up
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }
}

module.exports = Registration;
