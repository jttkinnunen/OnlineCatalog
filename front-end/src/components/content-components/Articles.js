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

<<<<<<< HEAD
class Article extends React.Component {


    state ={
        countO : 0,
        countH : 0,
        messages:[]

    };
    handleClickPlusO = () => {
        this.setState(({ countO }) => ({
            countO: countO + 1
        }));
    };
    handleClickPlusH = () => {
        this.setState(({ countH }) => ({
            countH: countH + 1
        }));
    };
    handleClickMinusO = () => {
        this.setState(({ countO }) => ({
            countO: countO - 1
        }));
    };
    handleClickMinusH = () => {
        this.setState(({ countH }) => ({
            countH: countH - 1
        }));
    };
    handleClickDoO = () => {

        //ToDo viedään tieto kantaan

    };
    handleClickDoH = () => {
       //ToDo viedään tieto kantaan

    };

    render() {

        return(
            <div className="main-content">
                <Row >

                    <Col sm="6">
                            <Card body className="text-left">
                                <Row>
                                    <Col>
                                        <CardTitle>Varasto: {this.props.data.storage1}</CardTitle>
                                        <CardSubtitle>Määrä: {this.props.data.count1}</CardSubtitle>
                                        <CardText>Muuta</CardText>
                                        <ButtonGroup>
                                            <Button onClick={this.handleClickMinusO}>-</Button>
                                            <Button onClick={this.handleClickDoO}>{this.state.countO}</Button>
                                            <Button onClick={this.handleClickPlusO}>+</Button>
                                        </ButtonGroup>
                                    </Col>

                                        <Col sm="6" className="text-right">
                                            <Layout />
                                        </Col>
                                </Row>
                            </Card>
                            <Card body className="text-left">
                                <Row>
                                    <Col>
                                    <CardTitle>Varasto: {this.props.data.storage2}</CardTitle>
                                    <CardSubtitle>Määrä: {this.props.data.count2}</CardSubtitle>
                                    <CardText>Muuta</CardText>
                                    <ButtonGroup>
                                        <Button onClick={this.handleClickMinusH}>-</Button>
                                        <Button onClick={this.handleClickDoH}> {this.state.countH} </Button>
                                        <Button onClick={this.handleClickPlusH}>+</Button>
                                    </ButtonGroup>
                                    </Col>

                                    <Col sm="6" className="text-right">
                                        <Layout/>
                                    </Col>
                                </Row>
                            </Card>
                    </Col>
                    <Col sm ="6">
                         <Card body className="align-content-center">

                            <CardImg top width="50%" src={this.props.data.image} alt="Card image cap" />
                            <CardTitle>Tittle ID: {this.props.data.name}</CardTitle>
                            <CardSubtitle>Title: {this.props.data.name}</CardSubtitle>
                            <CardText>{this.props.data.description}</CardText>

                        </Card>
                    </Col>
                </Row>
            </div>
        );
    }
}
=======
>>>>>>> 8aa34a565ea7ebee3195e649c0128e3b2d7a781e

export default Articles;
