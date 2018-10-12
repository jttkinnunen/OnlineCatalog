import React from "react";
import {Button} from 'reactstrap';
import Body from "../Body";



class Articles extends React.Component {

    render() {
        return (
            <div className="body" class="flex-body">

                {this.props.articles.map((article, i) => <Article key = {i}
                                                                  data = {article} />)}
                <Button className = "button">
                    Lisää uusi tuote
                </Button>
            </div>
        );
    }
}

class Article extends React.Component {
    render() {
        return(
            <tr>
                <td>{this.props.data.id}</td>
                <td>{this.props.data.name}</td>
                <td>{this.props.data.description}</td>
                <td>{this.props.data.image}</td>
            </tr>
        );
    }
}

export default Articles;