import React from "react";
import { Col, Button, Form, FormGroup, Label, FormFeedback, Progress, Input, Alert, FormText } from 'reactstrap';
import '../../css/AddArticle.css';
import '../../css/Buttons.css';
import '../../css/Text.css';
// TODO: All functionality, formatting

class AddArticle extends React.Component {



    render() {

        let success;

        return (
            <div className = "standard-dark-text-color" id="productform">
                <h5 className = "white-h5"><strong>Uuden artikkelin lisäys</strong></h5>
                <Form >
                    <FormGroup row>
                        <Label for="productName" id="labels" sm={2}>Nimi</Label>
                        <Col sm={10}>
                            <Input placeholder='Esim. "FCGTalent, Paita, Punainen, XL ..."' />
                        </Col>
                    </FormGroup>

                    <FormGroup row>
                        <Label for="exampleSelect" id="labels" sm={2}>Varasto</Label>
                        <Col sm={10}>
                            <Input type="select" name="select" id="locatioms" >
                            <option>Oulu</option>
                            <option>Helsinki</option>
                            <option>Hianola</option>
                            </Input>
                        </Col>
                    </FormGroup>

                    <FormGroup row>
                        <Label for="exampleText" id="labels" sm={2}>Kuvaus</Label>
                        <Col sm={10}>
                            <Input type="textarea" name="text" id="exampleText" />
                        </Col>
                    </FormGroup>

                    <FormGroup row>
                        <Label for="sendImage" id="labels" sm={2}>Kuva</Label>
                        <Col sm={10}>
                            <Input type="file" name="file" id="sendImage" />
                        </Col>
                    </FormGroup>
                    <FormGroup row>
                        <Label id="labels" sm={2}></Label>
                        <Col sm={10}>

                            Voit valita tuotteelle osuvan kuvan laitteeltasi.
                            Kuvan koko ei saa ylittää 999999 Mb
                        </Col>
                    </FormGroup>

                    <FormGroup check row>
                        <Col sm={{ size: 10, offset: 1 }}>
                            <Button className="btn-add-article">Lähetä</Button>
                        </Col>
                    </FormGroup>
                </Form>
               {/* <div className="text-center">75%</div>
                <Progress value={75} />*/}

            </div>
        );
    }
}

export default AddArticle;