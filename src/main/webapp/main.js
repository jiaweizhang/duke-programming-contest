const React = require('react');
const ReactDOM = require('react-dom');
const App = React.createFactory(require('./js/App'));

ReactDOM.render(<App />, document.getElementById('body'));
