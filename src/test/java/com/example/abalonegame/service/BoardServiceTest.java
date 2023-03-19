package com.example.abalonegame.service;


import static org.mockito.Mockito.mock;

class BoardServiceTest {
//    private final static Ball W = new Ball(Color.WHITE);
//    private final static Ball B = new Ball(Color.BLACK);
//
//    private BoardService boardService;
//    private BoardRepository boardRepository ;
//    private final Board mockBoard = mock(Board.class);
//    Field[][] gameBoard = {
//            {new Field(0, 0,true), new Field(1, 0,true), new Field(2, 0,true), new Field(3, 0,true), new Field(4, 0,true), new Field(5, 0,true), new Field(6, 0,true), new Field(7, 0,true), new Field(8, 0,true), new Field(9, 0,true), new Field(10, 0,true)}, //0
//            //null                                  //A                                 //B                     //C                             //D                         //E                             //F                             //G                                 //H                             //I          /                      /null
//            {new Field(0, 1,true), new Field(B, 1, 1), new Field(B, 2, 1), new Field(3, 1), new Field(4, 1), new Field(5, 1), new Field(6, 1,true), new Field(7, 1,true), new Field(8, 1,true), new Field(9, 1,true), new Field(10, 1,true)}, //1
//            {new Field(0, 2,true), new Field(B, 1, 2), new Field(B, 2, 2), new Field(3, 2), new Field(4, 2), new Field(5, 2), new Field(6, 2), new Field(7, 2,true), new Field(8, 2,true), new Field(9, 2,true), new Field(10, 2,true)}, //2
//            {new Field(0, 3,true), new Field(B, 1, 3), new Field(B, 2, 3), new Field(B, 3, 3), new Field(4, 3), new Field(5, 3), new Field(6, 3), new Field(7, 3), new Field(8, 3,true), new Field(9, 3,true), new Field(10, 3,true)}, //3
//            {new Field(0, 4,true), new Field(B, 1, 4), new Field(B, 2, 4), new Field(B, 3, 4), new Field(4, 4), new Field(5, 4), new Field(6, 4), new Field(7, 4), new Field(W, 8, 4), new Field(9, 4,true), new Field(10, 4,true)}, //4
//            {new Field(0, 5,true), new Field(B, 1, 5), new Field(B, 2, 5), new Field(B, 3, 5), new Field(4, 5), new Field(5, 5), new Field(6, 5), new Field(W, 7, 5), new Field(W, 8, 5), new Field(W, 9, 5), new Field(10, 5,true)}, //5
//            {new Field(0, 6,true), new Field(1, 6,true), new Field(B, 2, 6), new Field(3, 6), new Field(4, 6), new Field(5, 6), new Field(6, 6), new Field(W, 7, 6), new Field(W, 8, 6), new Field(W, 9, 6), new Field(10, 6,true)}, //6
//            {new Field(0, 7,true), new Field(1, 7,true), new Field(2, 7,true), new Field(3, 7), new Field(4, 7), new Field(5, 7), new Field(6, 7), new Field(W, 7, 7), new Field(W, 8, 7), new Field(W, 9, 7), new Field(10, 7,true)}, //7
//            {new Field(0, 8,true), new Field(1, 8,true), new Field(2, 8,true), new Field(3, 8,true), new Field(4, 8), new Field(5, 8), new Field(6, 8), new Field(7, 8), new Field(W, 8, 8), new Field(W, 9, 8), new Field(10, 8,true)}, //8
//            {new Field(0, 9,true), new Field(1, 9,true), new Field(2, 9,true), new Field(3, 9,true), new Field(4, 9,true), new Field(5, 9), new Field(6, 9), new Field(7, 9), new Field(W, 8, 9), new Field(W, 9, 9), new Field(10, 9,true)}, //9
//            {new Field(0, 10,true), new Field(1, 10,true), new Field(2, 10,true), new Field(3, 10,true), new Field(4, 10,true), new Field(5, 10,true), new Field(6, 10,true), new Field(7, 10,true), new Field(8, 10,true), new Field(9, 10,true), new Field(10, 10,true)} //10
//
//    };
//
//    @BeforeEach
//    void setUp() {
//        boardRepository = mock(BoardRepository.class);
//        boardService = new BoardService(boardRepository);
//    }
//    private void assignBoard(Field[][] field){
//        for (Field[] y : field) {
//            for (Field x : y){
//                x.setBoard(mockBoard);
//            }
//        }
//    }
//    @Test
//    void createBoard() {
//        Field[][] testBoard = BoardService.createBoard(mockBoard);
//        assignBoard(gameBoard);
//        assertArrayEquals(gameBoard, testBoard);
//    }
//
//    @Test
//    void testFieldToList() {
//        Field tempField1 = new Field(2, 2);
//        Field tempField2 = new Field(B, 5, 2);
//        Field tempField3 = new Field(7, 4,true);
//        Field[][] testBoard = {
//                {tempField1, tempField2},
//                {tempField3}
//        };
//        Set<Field> expected = new HashSet<>();
//        expected.add(tempField1);
//        expected.add(tempField2);
//        expected.add(tempField3);
//        Set<Field> actual = boardService.boardToFieldList(testBoard);
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    void testFieldToListFail() {
//        Field tempField1 = new Field(2, 2);
//        Field tempField2 = new Field(B, 5, 2);
//        Field tempField3 = new Field(7, 4,true);
//        Field[][] testBoard = {
//                {tempField1, tempField2},
//                {tempField3},
//                {new Field(5, 8)}
//        };
//        Set<Field> expected = new HashSet<>();
//        expected.add(tempField1);
//        expected.add(tempField2);
//        expected.add(tempField3);
//        Set<Field> actual = boardService.boardToFieldList(testBoard);
//        assertNotEquals(expected, actual);
//    }
}
