import React, {Component} from 'react';
import './css/App.css';
import './css/Text.css';
import Content from './components/Content.js';
import Events from './components/Events.js';
import Navigation_bar from './components/Navigation.js';

// TODO: You need to also change this in ActivateUser thanks to bad coding
const HOST = "http://localhost:8080";

// TODO: Env variables for IP, POST queries

class App extends Component {
    constructor(props) {
        super(props);
        this.setView = this.setView.bind(this);
        this.login = this.login.bind(this);
        this.postJsonRequest = this.postJsonRequest.bind(this);
        this.setUser = this.setUser.bind(this);
        this.getUsers = this.getUsers.bind(this);
        this.getArticles = this.getArticles.bind(this);
        this.setQuery = this.setQuery.bind(this);
        this.addUser = this.addUser.bind(this);
        this.forgotPassword = this.forgotPassword.bind(this);
        this.deleteUsers = this.deleteUsers.bind(this);
        this.addArticle = this.addArticle.bind(this);
        this.setSelectedArticleId = this.setSelectedArticleId.bind(this);
        this.updateArticle = this.updateArticle.bind(this);
        this.uploadImage = this.uploadImage.bind(this);
        this.getArticleDetailed = this.getArticleDetailed.bind(this);

        this.state = {
            // TODO: Tähän kaikki mahdolliset muuttujat mitä sivulla voi olla. Päivitetään alielementeille tarvittaessa.
            current_view: "login", // articles, activate-user, add-article, audit-log, article-detailed, login, manage-users, forgot-pass, change-pass, profile-page, add-user
            debugval: "",
            login_state: "",
            add_user_state: "",
            query: "",
            pass_reset_state: "",
            user: {
                first_name: "",
                last_name: "",
                token: "",
                email: "",
                admin: false,
            },
            users: [],
            locations: [],
            articles: [],//[{"id": "","name": "","image": "","description": "","locations": []},{"id": "","name": "","image": "","description": "","locations": []}],
            articles_filtered: [],
            events: Array(50).fill(null),
            selectedArticle: "",
            selectedArticle_state: "",
            id: "",
            token: "",
            add_article_state: "",
            article: {
                id: "",
                name: "",
                description: "",
            }
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

    deleteUsers(list) {
        let payload, a;

        payload = {token: null, users: []};
        payload.token = this.state.user.token;
        while(a = list.pop()){
            payload.users.push(a);
        }

        this.postJsonRequest("/deleteUsers", payload)
            .then((responseJson) => {

                //TODO: Think whether should check for errors :)

                // Update users list after done
                this.getUsers();
            })
            .catch(err => {
            //TODO: Think whether should check for errors :D
        })
    }

    getArticles() {
        this.postJsonRequest("/getArticles", {token: this.state.user.token})
        .then((responseJson) => {
            if (responseJson.hasOwnProperty('error') === false){
                this.setState({
                    articles: responseJson,
                });
            }
                // Set search filter to nothing
                this.setQuery(this.state.query);
        })
        .catch(err => {
            this.setState({
                debugval: this.state.debugval + " Error-fetching-articles:" + err,
            })
        })
    }

    setQuery(q){
        let query = q.toLowerCase();
        let temp_articles;

        // If query string has something on it, then filter. If not, display all.
        if (query !== ""){
                // Filter by finding substring in article name
                temp_articles = this.state.articles.filter(article => article.name.toLowerCase().includes(query))
        }
        else
            temp_articles = this.state.articles;

        if (temp_articles === undefined)
            temp_articles = [];

        this.setState({
            query: query,
            articles_filtered: temp_articles
        })
    }

    getLocations(){
        this.postJsonRequest("/getLocations", {token: this.state.user.token})
            .then((responseJson) => {

                this.setState({
                    locations: responseJson,
                })
            })
            .catch(err => {
                this.setState({
                    debugval: this.state.debugval + " Error-fetching-locations" + err,
                })
            })
    }



    // TODO: Back-end ei tue vielä
    forgotPassword(email){

        this.setState({pass_reset_state: "Lähetetään ..."});

        this.postJsonRequest("/startPasswordReset", {
            email: email,
        })
            .then((responseJson) => {
                if (responseJson.hasOwnProperty('error')) {
                    this.setState({pass_reset_state: "Tapahtui virhe"})
                }
                else
                this.setState({
                    pass_reset_state: "Pyyntö lähetetty, tarkista sähköpostisi.",
                })
            })
            .catch(err => {
                this.setState({
                    pass_reset_state: "Tapahtui virhe: " + err
                })
            })
    }

    addUser(user_email, first_name, last_name, admin){

        this.setState({add_user_state: "Lisätään ..."});

        this.postJsonRequest("/addUser", {
                token: this.state.user.token,
                first_name: first_name,
                last_name: last_name,
                email: user_email,
                admin: admin
            })
            .then((responseJson) => {
                if (responseJson.hasOwnProperty('error')) {
                    // TODO: Lue "error" sisältö ja anna tarkempi virhe

                    this.setState({add_user_state: "Käyttäjän lisääminen epäonnistui"});
                }
                else
                {
                    this.setState({add_user_state: "Käyttäjän lisääminen onnistui."});
                }
            })
            .catch(err => {
                this.setState({
                    add_user_state: "Tapahtui virhe: " + err
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
                    this.getArticles();
                    // Find out what locations exist
                    this.getLocations();
                }
                else{
                    if (responseJson.hasOwnProperty('error'))
                    this.setState({login_state: "Käyttäjätunnus ja salasana eivät täsmää"});
                        else
                    this.setState({login_state: "Yhteysongelma"});
                    // TODO: inform of unsuccessful login
                }
            })
            .catch(err => {
                this.setState({
                    login_state: "Yhteysongelma",
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
            this.getArticles();

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

    addArticle(name, description){

        this.setState({add_article_state: "Lisätään ..."});

        this.postJsonRequest("/addArticle", {
            token: this.state.user.token,
            name: name,
            description: description,

        })
            .then((responseJson) => {
                if (responseJson.hasOwnProperty('error')) {
                    // TODO: Lue "error" sisältö ja anna tarkempi virhe

                    this.setState({add_article_state: "Artikkelin lisääminen epäonnistui"});
                }
                else
                {
                    this.setState({add_article_state: "Artikkelin lisääminen onnistui."});
                }
            })
            .catch(err => {
                this.setState({
                    add_article_state: "Tapahtui virhe: " + err
                })
            })
    }

    setSelectedArticleId(id) {
        this.setState({
            selectedArticleId: id
        })

    }

    updateArticle(id, name, description){

        this.setState({update_article_state: "Päivitetään ..."});

        this.postJsonRequest("/updateArticle", {
            token: this.state.user.token,
            id: id,
            name: name,
            description: description,

        })
            .then((responseJson) => {
                if (responseJson.hasOwnProperty('error')) {
                    // TODO: Lue "error" sisältö ja anna tarkempi virhe

                    this.setState({update_article_state: "Artikkelin päivittäminen epäonnistui"});
                }
                else
                {
                    this.setState({update_article_state: "Artikkelin päivittäminen onnistui."});
                }
            })
            .catch(err => {
                this.setState({
                    update_article_state: "Tapahtui virhe: " + err
                })
            })
    }

    uploadImage( image){

        this.setState({add_article_state: "Lisätään kuvaa ..."});

        this.postJsonRequest("/uploadImage", {

            image: image,

        })
            .then((responseJson) => {
                if (responseJson.hasOwnProperty('error')) {
                    // TODO: Lue "error" sisältö ja anna tarkempi virhe

                    this.setState({add_article_state: "Artikkelin kuvan lisääminen epäonnistui"});
                }
                else
                {
                    this.setState({add_article_state: "Artikkelin kuvan lisääminen onnistui."});
                }
            })
            .catch(err => {
                this.setState({
                    add_article_state: "Tapahtui virhe: " + err
                })
            })
    }

    getArticleDetailed(id){

        this.setState({add_article_state: "Lisätään kuvaa ..."});

        this.postJsonRequest("/getArticlesDetailed", {
            id: id,

        })
            .then((responseJson) => {
                if (responseJson.hasOwnProperty('error')) {
                    // TODO: Lue "error" sisältö ja anna tarkempi virhe

                    this.setState({update_article_state: "Artikkelin hakeminen epäonnistui"});
                }
                else
                {
                    this.setState({update_article_state: "Artikkelin hakeminen onnistui."});
                }
            })
            .catch(err => {
                this.setState({
                    add_article_state: "Tapahtui virhe: " + err
                })
            })
    }

    render() {
        return (
            <div className="App">
                    <header className="App-header">

                        <div className = "navigation-bar" >
                            <Navigation_bar user = {this.state.user}
                                            setView = {this.setView}
                                            getUsers = {this.getUsers}
                                            current_view = {this.state.current_view}
                                            setQuery = {this.setQuery}
                            />
                        </div>
                        <Events current_view = {this.state.current_view} className = "events"/>
                        <div className = "body">
                            <div className = "content">
                                <Content login_state = {this.state.login_state}
                                         users = {this.state.users}
                                         user = {this.state.user}
                                         current_view = {this.state.current_view}
                                         articles = {this.state.articles_filtered}
                                         add_user_state = {this.state.add_user_state}
                                         setView = {this.setView}
                                         login = {this.login}
                                         addUser = {this.addUser}
                                         forgotPassword = {this.forgotPassword}
                                         pass_reset_state = {this.state.pass_reset_state}
                                         deleteUsers = {this.deleteUsers}
                                         addArticle = {this.addArticle}
                                         updateArticle = {this.updateArticle}
                                         uploadImage = {this.uploadImage}
                                         getArticleDetailed = {this.getArticleDetailed}
                                />
                            </div>
                        </div>
                       {/* <div className="footer">
                        <Footer current_view = {this.state.current_view} debugval = {this.state.debugval}/>
                        </div>*/}
                    </header>
            </div>
        );
    }
}

export default App;
