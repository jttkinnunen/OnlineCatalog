import React from "react";
import { Col, Button, Form, FormGroup, Label, FormFeedback, Input, FormText } from 'reactstrap';

// TODO: All functionality, formatting

class AddArticle extends React.Component {
    render() {
        return (
            <Form>
                <FormGroup>
                    <Label for="productName" sm={2}>Tuotenimi</Label>
                    <Input valid placeholder='Esim. "FCGTalent, Paita, Punainen, XL ..."' />
                    <FormFeedback valid tooltip>Tuotenimi on saatavilla</FormFeedback>
                </FormGroup>

                <FormGroup row>
                    <Label for="exampleSelect" sm={2}>Varasto</Label>
                    <Col sm={10}>
                        <Input type="select" name="select" id="exampleSelect" />
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
                        <FormText color="muted">
                            Voit valita tuotteelle osuvan kuva käyttämältäsi laitteelta.
                            Kuvan koko ei saa ylittää 999999 Mb
                        </FormText>
                    </Col>
                </FormGroup>

                <FormGroup check row>
                    <Col sm={{ size: 10, offset: 1 }}>
                        <Button>Lähetä</Button>
                    </Col>
                </FormGroup>
            </Form>
        );
    }
}

export default AddArticle;