import React from "react";
import Articles from './body-components/Articles.js';

// Täältä löytyy kaikenlaista:
// https://reactstrap.github.io/components/form/

class Body extends React.Component {

    render() {
        if (this.props.current_view === "articles")
            return (
                <Articles articles = {this.props.articles}/>
            );
        else return(
            <div></div>
        );
    }
}

export default Body;