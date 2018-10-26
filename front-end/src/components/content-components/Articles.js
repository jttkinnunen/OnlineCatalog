import React from "react";
import {Card, CardDeck, Button, CardImg, Col, Row, Container, CardSubtitle, CardText, CardBody,
    CardTitle } from 'reactstrap';

import '../../css/Articles.css';

class Articles extends React.Component {
    constructor(props) {
        super(props);
        this.openDetailed = this.openDetailed.bind(this);
        this.addArticle = this.addArticle.bind(this);
    }

    // Go to add-article page
    addArticle() {
        this.props.setView("add-article");
    }

    // Open detailed view of product when clicked
    openDetailed(id) {

    }

    // TODO: kuvan handlaus
    // TODO: Tasoita vasemmalle ylivaluneet kortit
    // TODO: pidä korttipakka keskitettynä

    render() {
        return (
        <div >
             <Container>
                 <Row className = "article-card-container">
                     {this.props.articles.map(function(item, key) {
                         return (
                             <Col  sm={{ offset: 0 }}>
                                 <Card className = "article-card ">
                                     <CardImg top width="100%" src="https://placeholdit.imgix.net/~text?txtsize=33&txt=256%C3%97180&w=256&h=180" alt="Card image cap" />
                                     <CardBody>
                                         <CardSubtitle>{item.name}</CardSubtitle>
                                         <CardText className="text-left">
                                             <br/>
                                             {item.locations.map(function(item, key) {
                                                 return (
                                                     <div>{item.name}: {item.quantity} kpl</div>
                                                 )
                                             })
                                             }
                                         </CardText>
                                     </CardBody>
                                 </Card>
                             </Col>
                         )
                     })
                     }

                 </Row>
            </Container>
                <Button className = "button align-self-left" onClick={this.addArticle}>
                    Lisää uusi tuote
                </Button>
        </div>
        );
    }
}

export default Articles;
