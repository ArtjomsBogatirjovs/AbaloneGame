let canvas;
let ctx;

const board = {
    drawBoard: function () {
        let color = boardColor;
        let keyIndex = 0;
        let index;
        for (const [key, value] of Object.entries(fieldDefinition)) {
            for (let i = 0; i < value; i++) {
                if (['i', 'g', 'h', 'f'].includes(key)) {
                    index = 9 - i;
                } else {
                    index = value - i;
                }
                hexagons[`${key}${index}`] = {
                    x: keyIndex * hexagonWidth,
                    y: (4.5 + value * 0.5 - i) * hexagonHeight,
                    color: color,
                };
            }
            keyIndex++;
        }
        for (const [key, value] of Object.entries(hexagons)) {
            board.drawHexagon(value.x, value.y, value.color);
        }
    },

    drawGameState: function (boardIds) {
        console.log(boardIds)
        if (boardIds === undefined) {
            boardIds = gameState;
        }
        console.log(boardIds)
        for (const [key, value] of Object.entries(boardIds)) {
            value.forEach((field) => {
                const img = new Image();
                img.src = `./assets/ball-${key}.svg`;
                img.onload = () => {
                    ctx.drawImage(
                        img,
                        hexagons[field].x + hexagonWidth * 1.8,
                        hexagons[field].y + hexagonHeight * 0.65,
                        hexagonWidth * 0.75,
                        hexagonHeight * 0.75
                    );
                };
            });
        }
    },
    drawHexagon: function (relativeX = 0, relativeY = 0, color = "#ff0000") {
        ctx.fillStyle = color;
        relativeX += hexagonWidth * 1.85;
        relativeY += hexagonHeight / 2;
        ctx.beginPath();
        ctx.moveTo(relativeX, relativeY);
        ctx.lineTo(relativeX  + scale, relativeY);
         ctx.lineTo(relativeX + 1.5 * scale, relativeY + hexagonHeight / 2);
         ctx.lineTo(relativeX + 1 * scale, relativeY + hexagonHeight);
         ctx.lineTo(relativeX + 0, relativeY + hexagonHeight);
         ctx.lineTo(relativeX - 0.5 * scale, relativeY + hexagonHeight / 2);
        ctx.closePath();
        ctx.fill();
    }
}

