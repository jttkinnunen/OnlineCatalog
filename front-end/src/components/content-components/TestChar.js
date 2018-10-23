import React from "react";
import Chart from "chart.js";

class Layout extends React.Component {
    constructor(props) {
        super(props);
    }

    componentDidMount() {
        const node = this.node;

        let myChart = new Chart(node, {
            type: "bar",
            data: {
                labels: ["Tammi", "Helmi", "Maalis"],
                datasets: [
                    {
                        label: "Viimekuut",
                        data: [12, 19, 3],
                        backgroundColor: [
                            "rgba(255, 99, 132, 0.2)",
                            "rgba(54, 162, 235, 0.2)",
                            "rgba(255, 206, 86, 0.2)"
                        ]
                    }
                ]
            }
        });
    }

    render() {
        return (
            <div>
                <canvas
                    style={{width: 400, height: 400}}
                    ref={node => (this.node = node)}
                />
            </div>
        );
    }
}
export default Layout;
