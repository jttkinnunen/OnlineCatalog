import React from "react";
import Articles from "./Articles";
import {Card, Button, CardImg, CardText, CardBody, CardTitle, Row, Col, ButtonGroup, CardSubtitle, Input } from 'reactstrap';
import NumericInput from 'react-numeric-input';
import Layout from "./TestChar";
import '../../css/ArticleDetailed.css';
import Form from "react-bootstrap/es/Form";
import FormGroup from "react-bootstrap/es/FormGroup";
import Label from "react-bootstrap/es/Label";


// TODO: korjaa tämä täysin, poista koodia tarvittaessa. Alla Juhan esimerkkikoodia joka oli aiemmin etusivulla

class ArticleDetailed extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            value: "",
            count: 0
        };
         this.handleIncrement=this.handleIncrement.bind(this);
         this.handleDecrement=this.handleDecrement.bind(this);
        this.onRadioBtnClick = this.onRadioBtnClick.bind(this);
    }

    onRadioBtnClick(rSelected) {
        this.setState({ rSelected });
    }

    handleIncrement =() => {
        this.setState({ count: this.state.count+1});

    };
    handleDecrement= () => {
        this.setState({ count: this.state.count-1});

    };

    render() {
        return(

            <div className="detailed-articles">

                <div className="card-holder">
                    <Card classname ="detailed">
                        <CardImg top width="100%" src="https://placeholdit.imgix.net/~text?txtsize=33&txt=256%C3%97180&w=256&h=180" alt="Card image cap" />
                        <CardBody>
                            <CardTitle>Card title</CardTitle>
                            <CardSubtitle>Card subtitle</CardSubtitle>
                            <CardText>Some quick example </CardText>
                            <Button>Button</Button>
                        </CardBody>
                    </Card>
                </div>
                <br/>

                <div className="container" id="warehouses">

                        <Button color="primary" id="rightbtn" onClick={() => this.onRadioBtnClick("Tuotteiden Otto")} active={this.state.rSelected === 1}>Otto</Button>
                        <Button color="success" id="leftbtn" onClick={() => this.onRadioBtnClick("Tuotteiden palautus")} active={this.state.rSelected === 2}>Palautus</Button>

                    <p>{this.state.rSelected}</p>

                    <br/>
                    <Input type="select" name="select" id="exampleSelect">
                        <option>Oulu</option>
                        <option>Helsinki</option>
                        <option>Hianola</option>
                    </Input>
                    <br/>



                    <span className="howmany">
                        {/*<Button id="decbtn" color="danger" onClick={() => {this.handleDecrement()}}>-</Button>*/}

                          <NumericInput
                              value={0}
                              precision={0}
                              size={15}
                              step={1}
                              height={30}
                              mobile={true} />



                        {/*<Button id="incbtn" color="primary" onClick={() => {this.handleIncrement()}}>+</Button>*/}


                    </span>
                </div>
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
            <div className="maincontent">
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
                        <Card body>

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

export default ArticleDetailed;