const React = require('react');
const problems = [
  {
    number: 1,
    points: 10,
    difficulty: 'Hard',
    title: 'Longest Palindromic Subsequence'
  },
  {
    number: 2,
    points: 5,
    difficulty: 'Medium',
    title: 'Knapsack Problem'
  },
  {
    number: 3,
    points: 5,
    difficulty: 'Easy',
    title: 'My Google Inteview Question'
  }
]

export class Problems extends React.Component{
  constructor(props) {
    super(props);
  }

  componentDidMount() {
    this.props.selectTab(2);
  }

  renderProblemPreviews() {
    let elements = [];
    problems.forEach((problem, index) => {
      const problemElement = (
        <div key={"problem-" + index} className="problem-preview card">
          <div className="problem-number-container">
            <div className="problem-number">{problem.number}</div>
          </div>
          <div className="problem-description">
            <div className="problem-title">{problem.title}</div>
            <div className="problem-subtitle">{problem.points} points • Difficulty: {problem.difficulty}</div>
          </div>
        </div>
      )
      elements.push(problemElement);
    });
    return elements;
  }

  render() {
    return (
      <div>
        {this.renderProblemPreviews()}
      </div>
    );
  }
}

module.exports = Problems;
