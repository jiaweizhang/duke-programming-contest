const React = require('react');
const { Link } = require('react-router');
const sidebarTabs = [
  {
    icon: "home",
    name: "Home"
  },
  {
    icon: "tasks",
    name: "Problems"
  },
  {
    icon: "users",
    name: "Groups"
  },
  {
    icon: "line-chart",
    name: "Leaderboard"
  },
  {
    icon: "info",
    name: "Help"
  }
];

export class Sidebar extends React.Component{
  constructor(props) {
    super(props);

    this.state = {
      hovered: -1
    }
  }

  mouseEnterTab(index) {
    this.setState({
      hovered: index
    });
  }

  mouseLeaveTab(index) {
    this.setState({
      hovered: -1
    });
  }

  renderTabs() {
    let elements = [];
    sidebarTabs.forEach((tab, index) => {
      elements.push(
        <Link key={tab.name + "-tab"} to={"/" + (tab.name.toLowerCase() !== "home" ? tab.name.toLowerCase() : "")}>
          <div
            className={
              this.props.tabSelected === index
              ? "sidebar-tab selected"
              : "sidebar-tab"
            }
            onMouseEnter={this.mouseEnterTab.bind(this,index)}
            onMouseLeave={this.mouseLeaveTab.bind(this, index)}>
            <div className="sidebar-tab-icon">
              <i className={"fa fa-" + tab.icon} aria-hidden="true"></i>
            </div>
            <div className={
              this.state.hovered !== index
              ? "sidebar-tab-label hidden"
              : "sidebar-tab-label"
            }>
              {tab.name}
            </div>
          </div>
        </Link>
      )
    });
    return elements;
  }

  render() {
    return (
      <div className="sidebar-nav">
        {this.renderTabs()}
      </div>
    );
  }
}

module.exports = Sidebar;
