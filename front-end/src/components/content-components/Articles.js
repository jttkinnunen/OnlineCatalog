import React from "react";
import {Card, CardDeck, Button, CardImg, CardSubtitle, CardText, CardBody,
    CardTitle } from 'reactstrap';

import './Articles.css';

class Articles extends React.Component {


    // TODO: kuvan handlaus
    // TODO: Tasoita vasemmalle sijainnit
    // TODO: keskitä korttipakka

    render() {
        return (

             <div class="jumbotron" div className="body">

                 <CardDeck className="card-deck">
                     {this.props.articles.map(function(item, key) {
                         return (
                             <Card className = "article-card">
                                 <CardImg top width="100%" src="https://placeholdit.imgix.net/~text?txtsize=33&txt=256%C3%97180&w=256&h=180" alt="Card image cap" />
                                 <CardBody>
                                     <CardSubtitle>{item.name}</CardSubtitle>
                                     <CardText className="text-left">
                                         {item.locations.map(function(item, key) {
                                             return (
                                                 <div>{item.name}: {item.quantity}</div>
                                             )
                                         })
                                         }
                                     </CardText>
                                 </CardBody>
                             </Card>
                         )
                     })
                     }

                 </CardDeck>
                 <Button className = "button">
                     Lisää uusi tuote
                 </Button>
            </div>
        );
    }
}


export default Articles;
