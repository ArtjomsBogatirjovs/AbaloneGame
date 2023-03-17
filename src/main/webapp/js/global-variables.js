let selectedField = [];
let localPlay = false;
let possibleActions = [];
let currentPlayer = "white";
let playerColor;
let check = false;
const scale = 60;
const hexagonHeight = scale * Math.cos(Math.PI / 6) * 2;
const hexagonWidth = 1.5 * scale;
const hexagons = {};
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
const defaultPawnPositionsAtGameInit = {
    "white": {
    "ball": ["i5", "i6", "i7", "i8", "i9", "h4", "h5", "h6", "h7", "h8", "h9", "g5", "g6", "g7"]
    },
    "black": {
    "ball": ["a1", "a2", "a3", "a4", "a5", "b1", "b2", "b3", "b4", "b5", "b6", "c3", "c4", "c5","e1","e2","e3","e4","e5","e6","e7","e8","e9"]
    }
};
// const defaultPawnPositionsAtGameInit = {
//     "white": {
//         "ball": ["i5"]
//     },
//     "black": {
//         "ball": ["a1", "a2", "a3", "a4", "a5", "c3", "c4", "c5"]
//     }
// };

let gameState = {
    white: ["i5", "i6", "i7", "i8", "i9", "h4", "h5", "h6", "h7", "h8", "h9", "g5", "g6", "g7"],
    black: ["a1", "a2", "a3", "a4", "a5", "b1", "b2", "b3", "b4", "b5", "b6", "c3", "c4", "c5"],
};
