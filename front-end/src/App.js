import React, { Component } from 'react';
import './App.css';
import Body from './components/Body.js';
import Events from './components/Events.js';
import Navigation_bar from './components/Navigation.js';
import Footer from './components/Footer.js';

const HOST = "http://localhost:8080";

class App extends Component {
    constructor(props) {
        super(props);
        this.setView = this.setView.bind(this);
        this.login = this.login.bind(this);
        this.postJsonRequest = this.postJsonRequest.bind(this);
        this.setUser = this.setUser.bind(this);
        this.state = {
            // TODO: Tähän kaikki mahdolliset muuttujat mitä sivulla voi olla. Päivitetään alielementeille tarvittaessa.
            current_view: "login", // articles, add-article, audit-log, article-detailed, login, manage-users, forgot-pass, change-pass, profile-page
            debugval: '',
            login_state: '',
            user: {
                first_name: "",
                last_name: "",
                token: "",
                email: "",
                admin: "1", // admin/user
            },
            users: [],  // format: {first_name: "Keijo", last_name: "Kepponen", account: "mail@service.com", rights: "admin"/"user"}
            articles: [
                {
                    "id": 1,
                    "name": "Paita, FCGTalent, Musta",
                    "description": "Todella hieno musta t-paita",
                    "image": "data:image/jpeg;base64,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",
                    "storage1": "Oulu",
                    "count1": 2,
                    "storage2": "Helsinki",
                    "count2": 8
                },
                {
                    "id": 2,
                    "name": "Paita, FCG, M",
                    "description": null,
                    "image": "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSF-SWE-ndBsBZpptP_uWsbFAiBRSr2LnCqcK-iDOkQ9DX74L3zuQ",
                    "storage1": "Oulu",
                    "count1": 23,
                    "storage2": "Helsinki",
                    "count2": 50
                },
                {
                    "id": 3,
                    "name": "Kynä, FCG, Musta",
                    "description": null,
                    "image": "data:image/jpeg;base64,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",
                    "storage1": "Oulu",
                    "count1": 200,
                    "storage2": "Helsinki",
                    "count2": 850
                },
                {
                    "id": 4,
                    "name": "Kynä, FCG, Punainen",
                    "description": null,
                    "image": "data:image/jpeg;base64,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",
                    "storage1": "Oulu",
                    "count1": 432,
                    "storage2": "Helsinki",
                    "count2": 728
                }
            ],
            events: Array(50).fill(null),
        };
    }

    postJsonRequest(path, payload){
        return(
            fetch(HOST + path, {
                method: "POST",
                //mode: "no-cors", // TODO: try without this line
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(payload),
            }))
        .then(response => response.json());
    }

    fetchArticles() {
        this.postJsonRequest("/getArticles", {token: this.state.user.token})
        .then((responseJson) => {
            this.setState({
                debugval: this.state.debugval + " ArticlesResponse",
            })
        })
        .catch(err => {
            this.setState({
                debugval: this.state.debugval + " Error-fetching-articles:" + err,
            })
        })
    }

    login(user, pass){
        this.postJsonRequest("/login",
            {
            "username": user,
            "password": pass
            })
            .then((responseJson) => {

                // If successful login
                if (responseJson.hasOwnProperty('token')) {
                    let newUser = Object.assign({}, this.state.user);
                    newUser.first_name = responseJson.first_name;
                    newUser.last_name = responseJson.last_name;
                    newUser.token = responseJson.token;
                    newUser.email = responseJson.email;
                    newUser.admin = responseJson.admin;

                    this.setState({
                        current_view: "articles",
                        debugval: "Logged in as " + responseJson.first_name + " " + responseJson.last_name + " admin?: " + responseJson.admin,
                        user: newUser,
                        login_state: ""
                    });
                    //this.setState({newUser});
                }
                else{
                    this.setState({login_state: "Käyttäjätunnus ja salasana eivät täsmää"});
                    // TODO: inform of unsuccessful login
                }
            })
            .catch(err => {
                this.setState({
                    debugval: this.state.debugval + " Error-fetching-token:" + err,
                })
            })
    }

    logout(){
        let newUser = Object.assign({}, this.state.user);
        newUser.token = null;
        this.setState({newUser});
    }

    setView(new_view) {
        if(new_view === "articles")
            this.fetchArticles();

        this.setState({
            current_view: new_view,
        })
    }

    setUser(token, name, account, rights) {
        let newUser = Object.assign({}, this.state.user)
        newUser.name = name;
        newUser.account = account;
        newUser.rights = rights;

        this.setState({newUser});
    }

    render() {
        return (
            <div className="App">
                <div className="flex-container">
                    <header className="App-header">

                        <div className = "navigation-bar" >
                            <Navigation_bar user = {this.state.user} setView = {this.setView} current_view = {this.state.current_view} />
                        </div>

                        <div className = "body">
                            <div className="event-bar">
                                <Events current_view = {this.state.current_view} />

                            </div>
                            <Body login_state = {this.state.login_state} user = {this.state.user} current_view = {this.state.current_view} articles = {this.state.articles} setView = {this.setView} login = {this.login}/>
                        </div>

                        <div className="footer">
                            <Footer current_view = {this.state.current_view} debugval = {this.state.debugval}/>
                        </div>

                    </header>
                </div>
            </div>
        );
    }
}

export default App;
