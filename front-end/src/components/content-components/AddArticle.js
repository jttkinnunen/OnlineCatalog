import React from "react";
import { Col, Button, Form, FormGroup, Label, FormFeedback, Progress, Input, Alert, FormText } from 'reactstrap';
import '../../css/AddArticle.css';
// TODO: All functionality, formatting

class AddArticle extends React.Component {



    render() {

        let success;

        return (
            <div className = "standard-text-color" id="productform">
                <Form >
                    <FormGroup row>
                        <Label for="productName" sm={2}>Tuotenimi</Label>
                        <Col sm={10}>
                            <Input valid placeholder='Esim. "FCGTalent, Paita, Punainen, XL ..."' />
                        </Col>
                        <FormFeedback valid tooltip>Tuotenimi on saatavilla</FormFeedback>
                    </FormGroup>

                    <FormGroup row>
                        <Label for="exampleSelect" sm={2}>Varasto</Label>
                        <Col sm={10}>
                            <Input type="select" name="select" id="exampleSelect" >
                            <option>Oulu</option>
                            <option>Helsinki</option>
                            <option>Hianola</option>
                            </Input>
                        </Col>
                    </FormGroup>

                    <FormGroup row>
                        <Label for="exampleText" sm={2}>Tuotteen kuvaus</Label>
                        <Col sm={10}>
                            <Input type="textarea" name="text" id="exampleText" />
                        </Col>
                    </FormGroup>

                    <FormGroup row>
                        <Label for="sendImage" sm={2}>Kuva</Label>
                        <Col sm={10}>
                            <Input type="file" name="file" id="sendImage" />
                        </Col>
                        <Col>
                            Voit valita tuotteelle osuvan kuvan laitteeltasi.
                            Kuvan koko ei saa ylittää 999999 Mb
                        </Col>
                    </FormGroup>

                    <FormGroup check row>
                        <Col sm={{ size: 10, offset: 1 }}>
                            <Button>Lähetä</Button>
                        </Col>
                    </FormGroup>
                </Form>
                <div className="text-center">75%</div>
                <Progress value={75} />

            </div>
        );
    }
}

export default AddArticle;