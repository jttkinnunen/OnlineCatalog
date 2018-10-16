import React from "react";
import {Card, CardImg, CardText, CardBody,
    CardTitle, CardSubtitle, Button, Col, Row, CardGroup, ButtonGroup, Media } from 'reactstrap';

import Layout from './TestChar'
import './Articles.css';

class Articles extends React.Component {


    render() {
        return (

             <div class="jumbotron" div className="body">

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
    handleClickMiinusO = () => {
        this.setState(({ countO }) => ({
            countO: countO - 1
        }));
    };
    handleClickMiinusH = () => {
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
            <Row >

                <Col sm="6">
                    <Card body className="text-left">
                        <Row>
                            <Col>
                                <CardTitle>Varasto: {this.props.data.storage1}</CardTitle>
                                <CardSubtitle>Määrä: {this.props.data.count1}</CardSubtitle>
                                <CardText>Muuta</CardText>
                                <ButtonGroup>
                                    <Button onClick={this.handleClickMiinusO}>-</Button>
                                    <Button onClick={this.handleClickDoO}>
                                        {this.state.countO}
                                    </Button>
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
                                <Button onClick={this.handleClickMiinusH}>-</Button>
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
                     <Card body>

                        <CardImg top width="50%" src={this.props.data.image} alt="Card image cap" />
                        <CardTitle>Tittle ID: {this.props.data.name}</CardTitle>
                        <CardSubtitle>Title: {this.props.data.name}</CardSubtitle>
                        <CardText>{this.props.data.description}</CardText>

                    </Card>
                </Col>
            </Row>
        );
    }
}

export default Articles;
