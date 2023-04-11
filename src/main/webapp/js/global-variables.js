let selectedField = [];
let historyInfo = [];
let gameState;
let gameStatus;
let color;
let opColor;

const winBalls = 5;
const boardColor = "#a8a098";
const scale = 50;
const hexagonHeight = scale * Math.cos(Math.PI / 6) * 2;
const hexagonWidth = 1.5 * scale;
const hexagons = {};
const direction = {
    x: 0,
    y: 0
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

const fieldEnum = {
    1: 'a',
    2: 'b',
    3: 'c',
    4: 'd',
    5: 'e',
    6: 'f',
    7: 'g',
    8: 'h',
    9: 'i',
};

const botType = "BOT";
const playerType = "HUMAN";






