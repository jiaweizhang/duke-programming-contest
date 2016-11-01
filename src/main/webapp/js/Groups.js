const React = require('react');
const $ = require("jquery");
const CONTAINER_WIDTH = 500;
const CONTAINER_HEIGHT = 450;
const TITLE_HEIGHT = 100;
const messages = {
  NOGROUP: 'You are not currently in a group.',
  CREATEGROUP: 'Create a new group',
  JOINGROUP: 'Join a group',
  LEAVEGROUP: 'Leave your group',
  CONFIRMLEAVE: 'Are you sure?'
}
const setup = {
  CREATE: 0,
  JOIN: 1,
  LEAVE: 2
}

export class Groups extends React.Component{
  constructor(props) {
    super(props);

    this.state = {
      setupIndex: -1,
      isSubmitting: false
    }

    const contestId = this.props.sessionData.contest.contestId;
    const authToken = this.props.sessionData.authToken;
    const currentGroup = $.ajax({
      contentType: 'application/json',
      url: 'api/groups/info/' + contestId,
      type: 'GET',
      beforeSend: (request) => {
        request.setRequestHeader('Authorization', authToken);
      }
    });

    $.when(currentGroup).done((response) => {
      console.log('response', response);
      if (response.success) {
        this.setState({
          currentGroup: response
        });
      }
    });
  }

  componentDidMount() {
    this.props.selectTab(2);
  }

  showSetup(setupOption) {
    console.log(setupOption);
    const panel = document.getElementById('sliding-setup-panel');
    panel.style.marginLeft = -CONTAINER_WIDTH + 'px';
    this.setState({
      setupIndex: setupOption
    });
    panel.classList.add('slide-panel');
    setTimeout(() => {
      panel.classList.remove('slide-panel');
    }, 500);
  }

  removeErrors() {

  }

  clearFields() {

  }

  returnFromSetup() {

    const panel = document.getElementById('sliding-setup-panel');
    panel.style.marginLeft = 0;
    panel.classList.add('slide-panel');
    setTimeout(() => {
      panel.classList.remove('slide-panel');
    }, 500);
  }

  createGroup() {
    console.log('create a group');
    let newGroupForm = document.getElementById('create-group-form');
    let newGroup = document.getElementById('group-name-field');
    let newGroupError = document.getElementById('create-group-error');

    console.log('name of new group:', newGroup.value);
    this.setState({
      isSubmitting: true
    }, () => {
      if (newGroup.value.length == 0) {
        console.log('SKT BLANK!');
        newGroupForm.classList.add('error');
        newGroupError.innerText = "Cannot be blank";
        this.setState({
          isSubmitting: false
        });
        return;
      }

      const groupNamePayload = {
        "groupName": newGroup.value
      }

      const groupNameCheck = $.ajax({
        contentType: 'application/json',
        url: 'api/quickfetch/groupName',
        type: 'POST',
        data: JSON.stringify(groupNamePayload)
      });

      $.when(groupNameCheck).done((response) => {
        console.log('response', response);
        if (response.exists) {
          newGroupForm.classList.add('error');
          newGroupError.innerText = "That group name already exists";
          this.setState({
            isSubmitting: false
          });
          return;
        } else {
          console.log('group created!')
          newGroupForm.classList.remove('error');
          const contestId = this.props.sessionData.contest.contestId;
          const authToken = this.props.sessionData.authToken;
          console.log('contest id:', contestId);
          const createGroup = $.ajax({
            beforeSend: (request) => {
              request.setRequestHeader('Authorization', authToken);
            },
            contentType: 'application/json',
            url: 'api/groups/create/' + contestId,
            type: 'POST',
            data: JSON.stringify(groupNamePayload)
          });

          $.when(createGroup).done((response) => {
            console.log('response', response);
            if (response.success) {
              this.setState({
                isSubmitting: false
              });
            }
          });
        }
      });
    });
  }

  joinGroup() {
    console.log('join a group');
    let newGroup = document.getElementById('group-name-field');

    console.log('name of new group:', newGroup.value);
    this.setState({
      isSubmitting: true
    }, () => {
      setTimeout(() => {
        this.setState({
          isSubmitting: false
        })
      }, 1000);
    });
  }

  leaveGroup() {
    console.log('leave your group');
    const contestId = this.props.sessionData.contest.contestId;
    const authToken = this.props.sessionData.authToken;
    console.log('contest id:', contestId);
    const leaveGroup = $.ajax({
      beforeSend: (request) => {
        request.setRequestHeader('Authorization', authToken);
      },
      contentType: 'application/json',
      url: 'api/groups/leave/' + contestId,
      type: 'POST'
    });
    $.when(leaveGroup).done((response) => {
      console.log('response', response);
    });
  }

  getSetupHeader() {
    switch (this.state.setupIndex) {
      case setup.CREATE:
        return messages.CREATEGROUP;
      case setup.JOIN:
        return messages.JOINGROUP;
      case setup.LEAVE:
        return messages.LEAVEGROUP;
    }
  }

  displaySetupForm() {
    switch (this.state.setupIndex) {
      case setup.CREATE:
        return (
          <div id="create-group-form" className="group-setup-form-container">
            <div className="group-name">
              <input
                type="text"
                id="group-name-field"
                className="text-field"
                placeholder="Group Name"/>
            </div>
            <div id="create-group-error" className="error-message">
            </div>
            <div
              onClick={this.createGroup.bind(this)}
              className={
                this.state.isSubmitting
                ? "submit-new-group link-button submitting"
                : "submit-new-group link-button"
              } style={{
                left: '82px',
                position: 'absolute'
              }}>
              {this.state.isSubmitting ? "Submitting..." : "Submit"}
            </div>
          </div>
        );
      case setup.JOIN:
        return (
          <div className="group-setup-form-container">
            <div className="group-name">
              <input
                type="text"
                id="group-name-field"
                className="text-field"
                placeholder="Group Name"/>
            </div>
            <div
              onClick={this.joinGroup.bind(this)}
              className={
                this.state.isSubmitting
                ? "submit-new-group link-button submitting"
                : "submit-new-group link-button"
              } style={{
                left: '82px',
                position: 'absolute'
              }}>
              {this.state.isSubmitting ? "Submitting..." : "Submit"}
            </div>
          </div>
        );
      case setup.LEAVE:
        return (
          <div className="group-setup-form-container">
            <div className="leave-group-confirmation">
              {messages.CONFIRMLEAVE}
            </div>
            <div className="leave-group-confirmation-buttons">
              <div
                onClick={this.leaveGroup.bind(this)}
                className="yes-leave link-button">Yes</div>
              <div
                onClick={this.returnFromSetup.bind(this)}
                className="no-leave link-button">
                  No
              </div>
            </div>
          </div>
        );
    }
  }

  render() {
    const width = this.props.dimensions.width - 160;
    const height = this.props.dimensions.height - 100;

    return (
      <div>
        <div className="groups-tab card" style={{
          width: width,
          height: height
        }}>
          <div className="tab-title">
            Groups
          </div>
          <div className="groups-tab-container" style={{
            width: CONTAINER_WIDTH + 'px',
            height: CONTAINER_HEIGHT + 'px',
            left: (width-CONTAINER_WIDTH)/2 + 'px',
            top: (height-CONTAINER_HEIGHT-TITLE_HEIGHT)/2 + TITLE_HEIGHT + 'px'
          }}>
            <div className="group-info">
              {
                this.state.currentGroup == null
                ? (
                  <div className="no-group">
                    messages.NOGROUP
                  </div>
                )
                : (
                  <div>
                    <div className="your-group-name">
                      {this.state.currentGroup.groupName}
                    </div>
                    <div className="your-group-members">
                      {this.state.currentGroup.memberNames}
                    </div>
                  </div>
                )
              }
            </div>
            <div className="group-setup-container" style={{
              width: CONTAINER_WIDTH + 'px'
            }}>
              <div id="sliding-setup-panel" className="sliding-setup-panel">
                <div style={{
                  width: CONTAINER_WIDTH + 'px'
                }}>
                  <div className="group-buttons" style={{
                    left: (CONTAINER_WIDTH-172)/2 + 'px'
                  }}>
                    {
                      this.state.currentGroup == null
                      ? (
                        <div>
                          <div
                            onClick={this.showSetup.bind(this, setup.CREATE)}
                            className="create-group link-button">
                            <i className="fa fa-plus" aria-hidden="true"></i>
                            &nbsp; Create Group
                          </div>
                          <div
                            onClick={this.showSetup.bind(this, setup.JOIN)}
                            className="join-group link-button">
                            <i className="fa fa-user-plus" aria-hidden="true"></i>
                            &nbsp; Join Group
                          </div>
                        </div>
                      ) : null
                    }
                    {
                      this.state.currentGroup != null
                      ? (
                        <div
                          onClick={this.showSetup.bind(this, setup.LEAVE)}
                          className="leave-group link-button">
                          <i className="fa fa-sign-out" aria-hidden="true"></i>
                          &nbsp; Leave Group
                        </div>
                      ) : null
                    }
                  </div>
                </div>
                <div className="group-setup-panel" style={{
                  width: CONTAINER_WIDTH + 'px',
                  left: CONTAINER_WIDTH + 'px'
                }}>
                  <div
                    onClick={this.returnFromSetup.bind(this)}
                    className="setup-back-button">
                    <i className="fa fa-chevron-circle-left" aria-hidden="true"></i>
                  </div>
                  <div className="group-setup-header">
                    {this.getSetupHeader()}
                  </div>
                  <div className="group-setup-form">
                    {this.displaySetupForm()}
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }
}

module.exports = Groups;
