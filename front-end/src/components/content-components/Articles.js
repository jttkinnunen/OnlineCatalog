import React from "react";
import {Card, CardDeck, Button, CardImg, Col, Row, Container, CardSubtitle, CardText, Alert, CardBody,
    CardTitle } from 'reactstrap';

import '../../css/Articles.css';
import '../../css/Text.css';


class Articles extends React.Component {
        constructor(props) {
        super(props);

        this.state = {
            result: "none", // none, success, failure
            selectedId: null

        };
        this.openDetailed = this.openDetailed.bind(this);
        this.addArticle = this.addArticle.bind(this);
        this.handleClick = this.handleClick.bind(this);
    }

    // Go to add-article page
    addArticle() {
        this.props.setView("add-article");
    }


    // Open detailed view of product when clicked
    openDetailed() {
        this.props.setSelectedArticleId(this.state.selectedId);
        console.log(this.props.selectedArticle);

        this.props.setView("article-detailed");
    }
    handleClick(item) {
        this.setState({
            selectedId: item
        });

        this.openDetailed();
        //console.log(id);
    }

    // TODO: kuvan handlaus
    // TODO: https://stackoverflow.com/questions/32802202/how-to-center-a-flex-container-but-left-align-flex-items

    render() {

let self = this;
        return (
        <div>
             <Container className = "article-main-container" >
                 <Row className = "article-card-container" >
                     {this.props.articles.map(function(item, key) {
                         return (
                             <Col  sm={{ offset: 0 }}>
                                 <Card  className = "article-card" key = {key} id={item.id} onClick={self.handleClick.bind(this,item.id)}  >

                                     <CardImg   top width="100%" src="https://placeholdit.imgix.net/~text?txtsize=33&txt=256%C3%97180&w=256&h=180" alt="Card image cap"  />
                                     <CardBody className = "article-card-body">
                                         <CardSubtitle >{item.name}</CardSubtitle>

                                         <CardText className="article-locations">
                                             {item.locations.map(function(item, i) {
                                                 return (
                                                     <div  key={i}>{item.name}: {item.quantity} kpl</div>
                                                 )
                                             })
                                             }
                                         </CardText>
                                     </CardBody>
                                 </Card>
                             </Col>
                         )
                     },self)
                     }
                 </Row>
            </Container>
            {/*
                <Button className = "button align-self-left" onClick={this.addArticle}>
                    Lisää uusi tuote
                </Button>
                */}
        </div>
        );
    }
}


export default Articles;
