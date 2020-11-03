package com.example.redoy.lynk.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by redoy.ahmed on 04-Mar-2018.
 */

public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "lynk";

    private static final String TUTORIAL_LANGUAGE = "TutorialLanguage";
    private static final String TUTORIAL_CATEGORY = "TutorialCategory";
    private static final String QUESTION_ANSWER = "QuestionAnswer";
    private static final String TUTORIAL_RESULT = "TutorialResult";

    private static final String KEY_NAME = "name";
    private static final String KEY_LANGUAGE_ID = "language_id";
    private static final String KEY_CATEGORY_ID = "category_id";
    private static final String KEY_TUTORIAL_CODE = "tutorial_code";
    private static final String KEY_TOTAL_QUESTION = "total_question";
    private static final String KEY_CORRECT_ANSWER = "correct_answer";
    private static final String KEY_TIMES_PLAYED = "times_played";

    private static final String KEY_ID = "id";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_QUES = "question";
    private static final String KEY_ANSWER = "answer";
    private static final String KEY_OPTONE = "option_one";
    private static final String KEY_OPTTWO = "option_two";
    private static final String KEY_OPTTHREE = "option_three";
    private static final String KEY_OPTFOUR = "option_four";
    private static final String KEY_LEVEL = "level";
    private SQLiteDatabase database;
    private Context context;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        database = db;
        String sql;

        sql = "CREATE TABLE IF NOT EXISTS "
                + TUTORIAL_LANGUAGE + " ( "
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_NAME + " TEXT);";
        db.execSQL(sql);

        sql = "CREATE TABLE IF NOT EXISTS "
                + TUTORIAL_CATEGORY + " ( "
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_LANGUAGE_ID + " INTEGER, "
                + KEY_CATEGORY + " TEXT, "
                + KEY_TUTORIAL_CODE + " TEXT, "
                + KEY_LEVEL + " INTEGER);";
        db.execSQL(sql);

        sql = "CREATE TABLE IF NOT EXISTS "
                + QUESTION_ANSWER + " ( "
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_CATEGORY_ID + " INTEGER, "
                + KEY_CATEGORY + " TEXT, "
                + KEY_QUES + " TEXT, "
                + KEY_OPTONE + " TEXT, "
                + KEY_OPTTWO + " TEXT, "
                + KEY_OPTTHREE + " TEXT, "
                + KEY_OPTFOUR + " TEXT, "
                + KEY_ANSWER + " INTEGER);";
        db.execSQL(sql);

        sql = "CREATE TABLE IF NOT EXISTS "
                + TUTORIAL_RESULT + " ( "
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_LANGUAGE_ID + " INTEGER, "
                + KEY_CATEGORY_ID + " INTEGER, "
                + KEY_TIMES_PLAYED + " INTEGER, "
                + KEY_TOTAL_QUESTION + " INTEGER, "
                + KEY_CORRECT_ANSWER + " INTEGER);";
        db.execSQL(sql);

        /*addLanguages();
        addCategories();
        addQuestions();*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + QUESTION_ANSWER);
        onCreate(sqLiteDatabase);
    }

    /*private void addLanguages() {
        Language l1 = new Language("Perl");
        addLanguage(l1);
    }

    private void addLanguage(Language l) {
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, l.getName());
        database.insert(TUTORIAL_LANGUAGE, null, values);
    }

    private void addCategories() {
        String[] category = context.getResources().getStringArray(R.array.category);
        String[] code = PractiseCodes.allCodes;
        Category c;
        int changeLevel = 0;
        int level = 1;
        for (int i = 0; i < category.length; i++) {
            changeLevel++;
            if (changeLevel % 5 == 0) {
                c = new Category(1, category[i], code[i], level++);
            } else {
                c = new Category(1, category[i], code[i], level);
            }
            addCategory(c);
        }
    }

    private void addCategory(Category c) {
        ContentValues values = new ContentValues();
        values.put(KEY_LANGUAGE_ID, c.getLanguage_id());
        values.put(KEY_CATEGORY, c.getCategory());
        values.put(KEY_TUTORIAL_CODE, c.getCode());
        values.put(KEY_LEVEL, c.getLevel());
        database.insert(TUTORIAL_CATEGORY, null, values);
    }

    private void addQuestions() {

        String[] category_id = context.getResources().getStringArray(R.array.tutorialDetails);
        String[] category = context.getResources().getStringArray(R.array.category);

        String[] introduction_questions = context.getResources().getStringArray(R.array.introduction_questions);
        String[] introduction_answers = context.getResources().getStringArray(R.array.introduction_answers);

        String[] environment_questions = context.getResources().getStringArray(R.array.environment_questions);
        String[] environment_answers = context.getResources().getStringArray(R.array.environment_answers);

        String[] syntax_overview_questions = context.getResources().getStringArray(R.array.syntax_overview_questions);
        String[] syntax_overview_answers = context.getResources().getStringArray(R.array.syntax_overview_answers);

        String[] data_types_questions = context.getResources().getStringArray(R.array.data_types_questions);
        String[] data_types_answers = context.getResources().getStringArray(R.array.data_types_answers);

        String[] variables_questions = context.getResources().getStringArray(R.array.variables_questions);
        String[] variables_answers = context.getResources().getStringArray(R.array.variables_answers);

        String[] scalars_questions = context.getResources().getStringArray(R.array.scalars_questions);
        String[] scalars_answers = context.getResources().getStringArray(R.array.scalars_answers);

        String[] arrays_questions = context.getResources().getStringArray(R.array.arrays_questions);
        String[] arrays_answers = context.getResources().getStringArray(R.array.arrays_answers);

        String[] hashes_questions = context.getResources().getStringArray(R.array.hashes_questions);
        String[] hashes_answers = context.getResources().getStringArray(R.array.hashes_answers);

        String[] if_else_questions = context.getResources().getStringArray(R.array.if_else_questions);
        String[] if_else_answers = context.getResources().getStringArray(R.array.if_else_answers);

        String[] loops_questions = context.getResources().getStringArray(R.array.loops_questions);
        String[] loops_answers = context.getResources().getStringArray(R.array.loops_answers);

        String[] operators_questions = context.getResources().getStringArray(R.array.operators_questions);
        String[] operators_answers = context.getResources().getStringArray(R.array.operators_answers);

        String[] date_and_time_questions = context.getResources().getStringArray(R.array.date_and_time_questions);
        String[] date_and_time_answers = context.getResources().getStringArray(R.array.date_and_time_answers);

        String[] subroutines_questions = context.getResources().getStringArray(R.array.subroutines_questions);
        String[] subroutines_answers = context.getResources().getStringArray(R.array.subroutines_answers);

        String[] references_questions = context.getResources().getStringArray(R.array.references_questions);
        String[] references_answers = context.getResources().getStringArray(R.array.references_answers);

        String[] formats_questions = context.getResources().getStringArray(R.array.formats_questions);
        String[] formats_answers = context.getResources().getStringArray(R.array.formats_answers);

        String[] files_questions = context.getResources().getStringArray(R.array.files_questions);
        String[] files_answers = context.getResources().getStringArray(R.array.files_answers);

        String[] directories_questions = context.getResources().getStringArray(R.array.directories_questions);
        String[] directories_answers = context.getResources().getStringArray(R.array.directories_answers);

        String[] error_handling_questions = context.getResources().getStringArray(R.array.error_handling_questions);
        String[] error_handling_answers = context.getResources().getStringArray(R.array.error_handling_answers);

        String[] special_variables_questions = context.getResources().getStringArray(R.array.special_variables_questions);
        String[] special_variables_answers = context.getResources().getStringArray(R.array.special_variables_answers);

        String[] regular_expression_questions = context.getResources().getStringArray(R.array.regular_expression_questions);
        String[] regular_expression_answers = context.getResources().getStringArray(R.array.regular_expression_answers);

        String[] socket_programming_questions = context.getResources().getStringArray(R.array.socket_programming_questions);
        String[] socket_programming_answers = context.getResources().getStringArray(R.array.socket_programming_answers);

        String[] oop_questions = context.getResources().getStringArray(R.array.oop_questions);
        String[] oop_answers = context.getResources().getStringArray(R.array.oop_answers);

        String[] database_access_questions = context.getResources().getStringArray(R.array.database_access_questions);
        String[] database_access_answers = context.getResources().getStringArray(R.array.database_access_answers);

        for (int i = 0; i < introduction_questions.length; i++) {
            String[] parts = introduction_answers[i].split(",");
            QuestionItem introductionQuestions = new QuestionItem(Integer.valueOf(category_id[0]), category[0], introduction_questions[i], parts[0], parts[item_image], parts[2], parts[3], Integer.valueOf(parts[4]));
            addQuestion(introductionQuestions);
        }

        for (int i = 0; i < environment_questions.length; i++) {
            String[] parts = environment_answers[i].split(",");
            QuestionItem environmentQuestions = new QuestionItem(Integer.valueOf(category_id[1]), category[1], environment_questions[i], parts[0], parts[1], parts[2], parts[3], Integer.valueOf(parts[4]));
            addQuestion(environmentQuestions);
        }

        for (int i = 0; i < syntax_overview_questions.length; i++) {
            String[] parts = syntax_overview_answers[i].split(",");
            QuestionItem syntaxOverviewQuestions = new QuestionItem(Integer.valueOf(category_id[2]), category[2], syntax_overview_questions[i], parts[0], parts[1], parts[2], parts[3], Integer.valueOf(parts[4]));
            addQuestion(syntaxOverviewQuestions);
        }

        for (int i = 0; i < data_types_questions.length; i++) {
            String[] parts = data_types_answers[i].split(",");
            QuestionItem dataTypesQuestions = new QuestionItem(Integer.valueOf(category_id[3]), category[3], data_types_questions[i], parts[0], parts[1], parts[2], parts[3], Integer.valueOf(parts[4]));
            addQuestion(dataTypesQuestions);
        }

        for (int i = 0; i < variables_questions.length; i++) {
            String[] parts = variables_answers[i].split(",");
            QuestionItem variablesQuestions = new QuestionItem(Integer.valueOf(category_id[4]), category[4], variables_questions[i], parts[0], parts[1], parts[2], parts[3], Integer.valueOf(parts[4]));
            addQuestion(variablesQuestions);
        }

        for (int i = 0; i < scalars_questions.length; i++) {
            String[] parts = scalars_answers[i].split(",");
            QuestionItem scalarsQuestions = new QuestionItem(Integer.valueOf(category_id[5]), category[5], scalars_questions[i], parts[0], parts[1], parts[2], parts[3], Integer.valueOf(parts[4]));
            addQuestion(scalarsQuestions);
        }

        for (int i = 0; i < arrays_questions.length; i++) {
            String[] parts = arrays_answers[i].split(",");
            QuestionItem arraysQuestions = new QuestionItem(Integer.valueOf(category_id[6]), category[6], arrays_questions[i], parts[0], parts[1], parts[2], parts[3], Integer.valueOf(parts[4]));
            addQuestion(arraysQuestions);
        }

        for (int i = 0; i < hashes_questions.length; i++) {
            String[] parts = hashes_answers[i].split(",");
            QuestionItem arraysQuestions = new QuestionItem(Integer.valueOf(category_id[7]), category[7], hashes_questions[i], parts[0], parts[1], parts[2], parts[3], Integer.valueOf(parts[4]));
            addQuestion(arraysQuestions);
        }

        for (int i = 0; i < if_else_questions.length; i++) {
            String[] parts = if_else_answers[i].split(",");
            QuestionItem ifElseQuestions = new QuestionItem(Integer.valueOf(category_id[8]), category[8], if_else_questions[i], parts[0], parts[1], parts[2], parts[3], Integer.valueOf(parts[4]));
            addQuestion(ifElseQuestions);
        }

        for (int i = 0; i < loops_questions.length; i++) {
            String[] parts = loops_answers[i].split(",");
            QuestionItem loopsQuestions = new QuestionItem(Integer.valueOf(category_id[9]), category[9], loops_questions[i], parts[0], parts[1], parts[2], parts[3], Integer.valueOf(parts[4]));
            addQuestion(loopsQuestions);
        }

        for (int i = 0; i < operators_questions.length; i++) {
            String[] parts = operators_answers[i].split(",");
            QuestionItem operatorsQuestions = new QuestionItem(Integer.valueOf(category_id[10]), category[10], operators_questions[i], parts[0], parts[1], parts[2], parts[3], Integer.valueOf(parts[4]));
            addQuestion(operatorsQuestions);
        }

        for (int i = 0; i < date_and_time_questions.length; i++) {
            String[] parts = date_and_time_answers[i].split(",");
            QuestionItem dateAndTimeQuestions = new QuestionItem(Integer.valueOf(category_id[11]), category[11], date_and_time_questions[i], parts[0], parts[1], parts[2], parts[3], Integer.valueOf(parts[4]));
            addQuestion(dateAndTimeQuestions);
        }

        for (int i = 0; i < subroutines_questions.length; i++) {
            String[] parts = subroutines_answers[i].split(",");
            QuestionItem subroutinesQuestions = new QuestionItem(Integer.valueOf(category_id[12]), category[12], subroutines_questions[i], parts[0], parts[1], parts[2], parts[3], Integer.valueOf(parts[4]));
            addQuestion(subroutinesQuestions);
        }

        for (int i = 0; i < references_questions.length; i++) {
            String[] parts = references_answers[i].split(",");
            QuestionItem referencesQuestions = new QuestionItem(Integer.valueOf(category_id[13]), category[13], references_questions[i], parts[0], parts[1], parts[2], parts[3], Integer.valueOf(parts[4]));
            addQuestion(referencesQuestions);
        }

        for (int i = 0; i < formats_questions.length; i++) {
            String[] parts = formats_answers[i].split(",");
            QuestionItem referencesQuestions = new QuestionItem(Integer.valueOf(category_id[14]), category[14], references_questions[i], parts[0], parts[1], parts[2], parts[3], Integer.valueOf(parts[4]));
            addQuestion(referencesQuestions);
        }

        for (int i = 0; i < files_questions.length; i++) {
            String[] parts = files_answers[i].split(",");
            QuestionItem filesQuestions = new QuestionItem(Integer.valueOf(category_id[15]), category[15], files_questions[i], parts[0], parts[1], parts[2], parts[3], Integer.valueOf(parts[4]));
            addQuestion(filesQuestions);
        }

        for (int i = 0; i < directories_questions.length; i++) {
            String[] parts = directories_answers[i].split(",");
            QuestionItem directoriesQuestions = new QuestionItem(Integer.valueOf(category_id[16]), category[16], directories_questions[i], parts[0], parts[1], parts[2], parts[3], Integer.valueOf(parts[4]));
            addQuestion(directoriesQuestions);
        }

        for (int i = 0; i < error_handling_questions.length; i++) {
            String[] parts = error_handling_answers[i].split(",");
            QuestionItem errorHandlingQuestions = new QuestionItem(Integer.valueOf(category_id[17]), category[17], error_handling_questions[i], parts[0], parts[1], parts[2], parts[3], Integer.valueOf(parts[4]));
            addQuestion(errorHandlingQuestions);
        }

        for (int i = 0; i < special_variables_questions.length; i++) {
            String[] parts = special_variables_answers[i].split(",");
            QuestionItem specialVariablesQuestions = new QuestionItem(Integer.valueOf(category_id[18]), category[18], special_variables_questions[i], parts[0], parts[1], parts[2], parts[3], Integer.valueOf(parts[4]));
            addQuestion(specialVariablesQuestions);
        }

        for (int i = 0; i < regular_expression_questions.length; i++) {
            String[] parts = regular_expression_answers[i].split(",");
            QuestionItem regularExpressionQuestions = new QuestionItem(Integer.valueOf(category_id[20]), category[20], regular_expression_questions[i], parts[0], parts[1], parts[2], parts[3], Integer.valueOf(parts[4]));
            addQuestion(regularExpressionQuestions);
        }

        for (int i = 0; i < socket_programming_questions.length; i++) {
            String[] parts = socket_programming_answers[i].split(",");
            QuestionItem socketProgrammingQuestions = new QuestionItem(Integer.valueOf(category_id[21]), category[21], socket_programming_questions[i], parts[0], parts[1], parts[2], parts[3], Integer.valueOf(parts[4]));
            addQuestion(socketProgrammingQuestions);
        }

        for (int i = 0; i < oop_questions.length; i++) {
            String[] parts = oop_answers[i].split(",");
            QuestionItem oopQuestions = new QuestionItem(Integer.valueOf(category_id[22]), category[22], oop_questions[i], parts[0], parts[1], parts[2], parts[3], Integer.valueOf(parts[4]));
            addQuestion(oopQuestions);
        }

        for (int i = 0; i < database_access_questions.length; i++) {
            String[] parts = database_access_answers[i].split(",");
            QuestionItem databaseAccessQuestions = new QuestionItem(Integer.valueOf(category_id[22]), category[22], database_access_questions[i], parts[0], parts[1], parts[2], parts[3], Integer.valueOf(parts[4]));
            addQuestion(databaseAccessQuestions);
        }
    }

    private void addQuestion(QuestionItem questionItem) {
        ContentValues values = new ContentValues();
        values.put(KEY_CATEGORY_ID, questionItem.getCategoryID());
        values.put(KEY_CATEGORY, questionItem.getCategory());
        values.put(KEY_QUES, questionItem.getQuestion());
        values.put(KEY_OPTONE, questionItem.getOption_one());
        values.put(KEY_OPTTWO, questionItem.getOption_two());
        values.put(KEY_OPTTHREE, questionItem.getOption_three());
        values.put(KEY_OPTFOUR, questionItem.getOption_four());
        values.put(KEY_ANSWER, questionItem.getAnswer());

        database.insert(QUESTION_ANSWER, null, values);
    }

    public void addResult(Result result) {
        database.delete(TUTORIAL_RESULT, KEY_CATEGORY_ID + "=?", new String[]{String.valueOf(result.getCategory_id())});

        ContentValues values = new ContentValues();
        values.put(KEY_LANGUAGE_ID, result.getLanguage_id());
        values.put(KEY_CATEGORY_ID, result.getCategory_id());
        values.put(KEY_TIMES_PLAYED, result.getTimes_played());
        values.put(KEY_TOTAL_QUESTION, result.getTotal_question());
        values.put(KEY_CORRECT_ANSWER, result.getCorrect_answer());
        database.insert(TUTORIAL_RESULT, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS " + QUESTION_ANSWER);
        onCreate(db);
    }

    public List<QuestionItem> getAllQuestions() {

        List<QuestionItem> quesList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + QUESTION_ANSWER + ";";

        database = this.getReadableDatabase();

        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                QuestionItem questionItem = new QuestionItem();
                questionItem.setId(cursor.getInt(0));
                questionItem.setCategoryID(cursor.getInt(1));
                questionItem.setCategory(cursor.getString(2));
                questionItem.setQuestion(cursor.getString(3));
                questionItem.setOption_one(cursor.getString(4));
                questionItem.setOption_two(cursor.getString(5));
                questionItem.setOption_three(cursor.getString(6));
                questionItem.setOption_four(cursor.getString(7));
                questionItem.setAnswer(cursor.getInt(8));
                quesList.add(questionItem);
            } while (cursor.moveToNext());
        }

        return quesList;
    }

    public List<Category> getAllTutorials() {

        List<Category> tutorialList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TUTORIAL_CATEGORY + ";";

        database = this.getReadableDatabase();

        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Category categoryItems = new Category();
                categoryItems.setId(cursor.getInt(0));
                categoryItems.setLanguage_id(cursor.getInt(1));
                categoryItems.setCategory(cursor.getString(2));
                categoryItems.setCode(cursor.getString(3));
                categoryItems.setLevel(cursor.getInt(4));
                tutorialList.add(categoryItems);
            } while (cursor.moveToNext());
        }

        return tutorialList;
    }

    public List<Category> getTutorialDetailsByCategory(String category) {

        List<Category> tutorialList = new ArrayList<>();

        String selectQuery = "select * from TutorialCategory where category='" + category + "';";

        database = this.getReadableDatabase();

        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Category categoryItems = new Category();
                categoryItems.setId(cursor.getInt(0));
                categoryItems.setLanguage_id(cursor.getInt(1));
                categoryItems.setCategory(cursor.getString(2));
                categoryItems.setCode(cursor.getString(3));
                categoryItems.setLevel(cursor.getInt(4));
                tutorialList.add(categoryItems);
            } while (cursor.moveToNext());
        }

        return tutorialList;
    }

    public List<PerformanceItem> getAllCategories() {

        List<PerformanceItem> categoryList = new ArrayList<>();

        String selectQuery = "" +
                "SELECT TC.language_id as languageID,\n" +
                "TC.id as categoryID,\n" +
                "TC.category as category,\n" +
                "COALESCE(TR.total_question, 0) AS totalQuestion,\n" +
                "COALESCE(TR.correct_answer, 0) AS correctAnswer \n" +
                "FROM\n" +
                "TutorialCategory TC\n" +
                "LEFT JOIN\n" +
                "TutorialResult TR\n" +
                "ON TC.id=TR.category_id;";

        database = this.getReadableDatabase();

        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                PerformanceItem performanceItem = new PerformanceItem();
                performanceItem.setLanguageID(cursor.getInt(0));
                performanceItem.setCategoryID(cursor.getInt(1));
                performanceItem.setCategory(cursor.getString(2));
                performanceItem.setTotalQuestion(cursor.getInt(3));
                performanceItem.setCorrectAnswer(cursor.getInt(4));
                categoryList.add(performanceItem);
            } while (cursor.moveToNext());
        }
        return categoryList;
    }

    public List<QuestionItem> getQuestionsByCategory(int category) {

        List<QuestionItem> quesList = new ArrayList<>();

        String selectQuery = "" +
                "select\n" +
                "*\n" +
                "from \n" +
                "QuestionAnswer \n" +
                "where category_id=" + category + " order by RANDOM();";

        database = this.getReadableDatabase();

        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                QuestionItem questionItem = new QuestionItem();
                questionItem.setId(cursor.getInt(0));
                questionItem.setCategoryID(cursor.getInt(1));
                questionItem.setCategory(cursor.getString(2));
                questionItem.setQuestion(cursor.getString(3));
                questionItem.setOption_one(cursor.getString(4));
                questionItem.setOption_two(cursor.getString(5));
                questionItem.setOption_three(cursor.getString(6));
                questionItem.setOption_four(cursor.getString(7));
                questionItem.setAnswer(cursor.getInt(8));
                quesList.add(questionItem);
            } while (cursor.moveToNext());
        }

        return quesList;
    }

    public List<Result> getResultsByCategory(int category_id) {

        List<Result> resultList = new ArrayList<>();

        String selectQuery = "" +
                "SELECT TR.id as id,\n" +
                "TR.language_id as language_id,\n" +
                "TC.id as categoryID,\n" +
                "TR.times_played as times_played,\n" +
                "COALESCE(TR.total_question, 0) AS totalQuestion,\n" +
                "COALESCE(TR.correct_answer, 0) AS correctAnswer,\n" +
                "TC.level as level\n" +
                "FROM\n" +
                "TutorialCategory TC\n" +
                "LEFT JOIN\n" +
                "TutorialResult TR\n" +
                "ON TC.id=TR.category_id where TR.category_id=" + category_id + ";";

        database = this.getReadableDatabase();

        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Result result = new Result();
                result.setId(cursor.getInt(0));
                result.setLanguage_id(cursor.getInt(1));
                result.setCategory_id(cursor.getInt(2));
                result.setTimes_played(cursor.getInt(3));
                result.setTotal_question(cursor.getInt(4));
                result.setCorrect_answer(cursor.getInt(5));
                result.setLevel(cursor.getInt(6));
                resultList.add(result);
            } while (cursor.moveToNext());
        }

        return resultList;
    }

    public int questionCount(int category_id) {
        int row;
        String selectQuery = "select * from QuestionAnswer where category_id=" + category_id + ";";
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        row = cursor.getCount();

        return row;
    }

    public int getQuizLevelByCategory(String category) {
        int level = 0;
        String selectQuery = "select level from TutorialCategory where category='" + category + "';";
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                level = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        return level;
    }*/
}