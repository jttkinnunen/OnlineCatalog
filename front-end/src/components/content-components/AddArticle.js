import React from "react";
import { Col, Button, Form, FormGroup, Label, FormFeedback, Progress, Input, Alert, FormText} from 'reactstrap';
import '../../css/AddArticle.css';
import '../../css/Buttons.css';
import '../../css/Text.css';
import Dropzone from 'react-dropzone';
const imageMaxSize = 1000000000 // bytes
const acceptedFileTypes = 'image/png, image/jpg, image/jpeg'
const acceptedFiletypesArray = acceptedFileTypes.split(",").map((item) => {return item.trim()})

//import axios from 'axios';
// TODO: All functionality, formatting

class AddArticle extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            productName: '',
            productDescription: '',
            productImageName: '',
            productImage: null

        };
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleChange = this.handleChange.bind(this);
    }
    handleSubmit() {
        console.log(this.state.productName, this.state.productDescription)
        this.props.addArticle(this.state.productName, this.state.productDescription);
        //this.props.getArticleDetailed()
        //console.log(token)
        this.props.uploadImage(this.state.productImage)
        console.log(this.props.update_article_state)

    }
    handleChange(event) {
        const target = event.target;
        const value = target.type === 'checkbox' ? target.checked : target.value;
        const name = target.name;

        this.setState({
            [name]: value
        })
    }

    fileSelectionHandler = event => {
        console.log(event.target.files[0]);
        this.setState({
            productImage: event.target.files[0],
            productImageName: this.state.productImage.name
        })

    }

    verifyFile = (files) => {
        if (files && files.length > 0){
            const currentFile = files[0]
            const currentFileType = currentFile.type
            const currentFileSize = currentFile.size
            if (currentFileSize > imageMaxSize) {
                alert("Liian suuri kuva. " + currentFileSize + " tavua liian suuri")
                return false
            }
            return true
        }
    }
    handleOnDrop = (files, rejectedFiles) => {
        if (rejectedFiles && rejectedFiles.length > 0){
            console.log(rejectedFiles)
            this.verifyFile(rejectedFiles)
        }
        if (files && files.length > 0) {
            const isVerified = this.verifyFile(files)
            if (isVerified){
                //imageBase64Data
                const currentFile = files[0]
                const myFileItemReader = new FileReader()
                myFileItemReader.addEventListener("load", ()=>{
                    console.log(myFileItemReader.result)
                    this.setState({
                        productImage: myFileItemReader.result
                    })
                }, false)

                myFileItemReader.readAsDataURL(currentFile)
            }
        }
    }


    render() {
        const {productImage} = this.state
        let success;
        let image_result = "";

        if (this.props.add_article_state !== ""){
            image_result =
                this.props.add_article_state
        }

        return (
            <div className = "standard-dark-text-color" id="productform">
                <h5 className = "white-h5"><strong>Uuden artikkelin lisäys</strong></h5>
                <Form >
                    <FormGroup row>
                        <Label for="productName" id="labels" sm={2}>Nimi</Label>
                        <Col sm={10}>
                           <Input type="text" name="productName" id="productName" placeholder='Artikkelin nimi Esim. "FCGTalent, Paita, Punainen, XL ..."' value = {this.state.productName} onChange = {this.handleChange}/>
                        </Col>
                    </FormGroup>

                    <FormGroup row>
                        <Label for="productDescription" id="labels" sm={2}>Kuvaus</Label>
                        <Col sm={10}>

                            <Input type="textarea" name="productDescription" id="productDescription" placeholder="Artikkelin kuvaus max 500 merkkiä" value ={this.state.productDescription} onChange = {this.handleChange}/>
                        </Col>
                    </FormGroup>

                    <FormGroup row>
                        <Label for="productImage" id="labels" sm={2}>Kuva</Label>
                        <div class="center">
                        <Col sm={10}> {productImage !== null ? <img src={productImage} /> : ''
                            } <Dropzone id="dropZone" onDrop={this.handleOnDrop} accept={acceptedFileTypes} multiple={false} maxSize={imageMaxSize}> Valitse/ vaihda kuva klikkaamalla tätä aluetta.  </Dropzone></Col>
                            {console.log(this.props.add_article_state)}</div>
                    </FormGroup>
                    <FormGroup row>
                    <Label id="labels" sm={2}></Label>
                    <Col sm={10} id="labels">

                        Voit valita tuotteelle osuvan kuvan laitteeltasi.
                        Kuvan koko ei saa ylittää {imageMaxSize} tavua
                    </Col>
                </FormGroup>

                    <FormGroup check row>
                        <Col sm={{ size: 10, offset: 1 }}>
                            <Button  className="btn-primary btn-wide" onClick={this.handleSubmit}>Lähetä</Button>
                        </Col>
                    </FormGroup>
                </Form>


            </div>
        );
    }
}

export default AddArticle;
