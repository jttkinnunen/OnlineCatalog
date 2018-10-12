import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';
import Body from './components/Body.js';
import Events from './components/Events.js';
import Navigation_bar from './components/Navigation.js';
import Footer from './components/Footer.js';

class App extends Component {
    constructor(props) {
        super(props);
        this.state = {
            // Tähän kaikki mahdolliset muuttujat mitä sivulla voi olla. Päivitetään alielementeille tarvittaessa.

            current_view: "articles", // articles, article-detailed, login, manage-users, forgot-pass, change-pass, profile-page
            user: null,
            user_token: null,
            user_rights: null,
            articles: [
                {
                    "id": 1,
                    "name": "Kynä, FCGTalent, Punainen",
                    "description": null,
                    "image": null
                },
                {
                    "id": 2,
                    "name": "Paita, FCG, M",
                    "description": null,
                    "image": null
                },
                {
                    "id": 3,
                    "name": "Paita, FCG, L",
                    "description": null,
                    "image": null
                },
                {
                    "id": 4,
                    "name": "Paita, FCG, XL",
                    "description": null,
                    "image": null
                }
            ],
            events: Array(50).fill(null),
        };
    }

    render() {
        return (
            <div className="App">

                <header className="App-header">

                    <div class="container-fluid" className = "navigation-bar">
                        <Navigation_bar />
                    </div>

                    <div class="container" className = "body">
                        <div className="container-fluid" className="event-bar">
                            <Events/>
                        </div>
                        <Body  current_view = {this.state.current_view} articles = {this.state.articles}/>
                    </div>

                    <div className="container-fluid" className="footer">
                        <Footer/>
                    </div>

                </header>
            </div>
        );
    }
}

export default App;
