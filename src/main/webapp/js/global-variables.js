let selectedField = [];
const boardColor = "#a8a098";
const scale = 50;
const hexagonHeight = scale * Math.cos(Math.PI / 6) * 2;
const hexagonWidth = 1.5 * scale;
const hexagons = {};
const direction = {
    x:0,
    y:0
}
const fieldDefinition = {
    a: 5,
    b: 6,
    c: 7,
    d: 8,
    e: 9,
    f: 8,
    g: 7,
    h: 6,
    i: 5,
};

let gameState;


