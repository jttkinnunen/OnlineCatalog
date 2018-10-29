import React from "react";
import { Container, Row, Col } from 'reactstrap';
import '../css/Footer.css';
class Footer extends React.Component {

    render() {
        return (
            <div className="event-text" id="fuutteri">
                <Container>
                    <Row>
                        <Col xs="6"><strong>FCG Talent Oy</strong></Col>
                        <Col xs="6"><strong>Tekijätiimi:</strong></Col>
                    </Row>
                    <Row>
                        <Col xs="6">Osmontie 34, 00610 Helsinki</Col>
                        <Col xs="6">nimi1</Col>
                    </Row>
                    <Row>
                        <Col xs="6">puh. +358 9 771 2627</Col>
                        <Col xs="6">nimi2</Col>
                    </Row>
                    <Row>
                        <Col xs="6">etu.suku@fcg.fi</Col>
                        <Col xs="6">nimi3</Col>
                    </Row>
                    <Row>
                        <Col xs="6">http://www.fcg.fi/</Col>
                        <Col xs="6">nimi4</Col>
                    </Row>
                </Container>
                {/*Current view: {this.props.current_view} Debug: {this.props.debugval}*/}
            </div>
        );
    }
}

export default Footer;