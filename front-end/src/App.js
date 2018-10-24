import React, { Component } from 'react';
import './App.css';
import Content from './components/Content.js';
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
        this.getUsers = this.getUsers.bind(this);
        this.fetchArticles = this.fetchArticles.bind(this);

        this.state = {
            // TODO: Tähän kaikki mahdolliset muuttujat mitä sivulla voi olla. Päivitetään alielementeille tarvittaessa.
            current_view: "login", // articles, add-article, audit-log, article-detailed, login, manage-users, forgot-pass, change-pass, profile-page, add-user
            debugval: '',
            login_state: '',
            user: {
                first_name: "",
                last_name: "",
                token: "",
                email: "",
                admin: false, // admin/user
            },
            users: [],
            articles: [{"id": "","name": "","image": "","description": "","locations": []},{"id": "","name": "","image": "","description": "","locations": []}],
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
            /*let newArticles = Object.assign({}, this.state.articles);
            newArticles.id = responseJson.id;
            newArticles.name = responseJson.name;
            newArticles.description = responseJson.description;
            newArticles.image = responseJson.image;
            newArticles.locations = responseJson.locations;*/

            this.setState({
                articles: responseJson,
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

                    // Refresh articles page
                    this.fetchArticles()
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

    getUsers(){
        this.postJsonRequest("/getUsers",
            {
                "token": this.state.user.token
            })
            .then((responseJson) => {
                this.setState({
                    users: responseJson,
                });
            })
            .catch(err => {
                this.setState({
                    debugval: this.state.debugval + " Error-getting-users:" + err,
                })
            })
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

                    <header className="App-header">

                        <div className = "navigation-bar" >
                            <Navigation_bar user = {this.state.user} setView = {this.setView} getUsers = {this.getUsers} current_view = {this.state.current_view} />
                        </div>

                        <div className="flex-container">
                            <div className = "body">
                                <Events current_view = {this.state.current_view} />
                                <Content login_state = {this.state.login_state}
                                         users = {this.state.users}
                                         user = {this.state.user}
                                         current_view = {this.state.current_view}
                                         articles = {this.state.articles}
                                         setView = {this.setView}
                                         login = {this.login}
                                />
                            </div>
                        </div>
                            <div className="footer">
                            <Footer current_view = {this.state.current_view} debugval = {this.state.debugval}/>
                            </div>
                    </header>
            </div>
        );
    }
}

export default App;
