package com.duolingo.app.utils;

import com.duolingo.app.models.ExamQuestion;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExamDataRepository {

    public static List<ExamQuestion> getQuestions(String difficulty) {
        List<ExamQuestion> list = new ArrayList<>();

        if (difficulty.equals("EASY")) {
            // Mức Dễ: 10 câu - Tập trung từ vựng & ngữ pháp cơ bản
            list.add(new ExamQuestion("______ is your favorite teacher?", Arrays.asList("What", "Who", "Where", "Which"), 1, "Who dùng để hỏi về người."));
            list.add(new ExamQuestion("I have ______ apple in my bag.", Arrays.asList("a", "an", "the", "any"), 1, "Apple bắt đầu bằng nguyên âm nên dùng 'an'."));
            list.add(new ExamQuestion("My father ______ to work every day.", Arrays.asList("go", "goes", "going", "gone"), 1, "Chủ ngữ số ít (My father) chia động từ thêm 'es'."));
            list.add(new ExamQuestion("There ______ some books on the table.", Arrays.asList("is", "am", "are", "be"), 2, "Books là số nhiều nên dùng 'are'."));
            list.add(new ExamQuestion("We go ______ school by bus.", Arrays.asList("to", "at", "in", "on"), 0, "Go to school: đi học."));
            list.add(new ExamQuestion("What ______ she doing now?", Arrays.asList("is", "am", "are", "do"), 0, "Thì hiện tại tiếp diễn với chủ ngữ 'she'."));
            list.add(new ExamQuestion("This is ______ biggest house in the street.", Arrays.asList("a", "an", "the", "most"), 2, "So sánh nhất luôn đi với 'the'."));
            list.add(new ExamQuestion("I ______ play football when I was young.", Arrays.asList("can", "could", "will", "may"), 1, "Diễn tả khả năng trong quá khứ dùng 'could'."));
            list.add(new ExamQuestion("She ______ a new car last week.", Arrays.asList("buy", "buys", "bought", "buying"), 2, "Last week là dấu hiệu thì quá khứ đơn."));
            list.add(new ExamQuestion("Listen! Someone ______ at the door.", Arrays.asList("knock", "knocks", "is knocking", "knocking"), 2, "Hành động đang xảy ra tại thời điểm nói."));

        } else if (difficulty.equals("MEDIUM")) {
            // Mức Trung bình: 15 câu - Cấp độ B1/B2
            list.add(new ExamQuestion("If I ______ you, I would take that job.", Arrays.asList("am", "was", "were", "would be"), 2, "Câu điều kiện loại 2."));
            list.add(new ExamQuestion("She has been living here ______ five years.", Arrays.asList("for", "since", "in", "during"), 0, "For + khoảng thời gian."));
            list.add(new ExamQuestion("He is interested ______ joining the club.", Arrays.asList("on", "at", "in", "with"), 2, "Cấu trúc be interested in."));
            list.add(new ExamQuestion("The man ______ lives next door is a doctor.", Arrays.asList("who", "whom", "which", "whose"), 0, "Mệnh đề quan hệ chỉ người làm chủ ngữ."));
            list.add(new ExamQuestion("By the time we arrived, the train ______.", Arrays.asList("leaves", "left", "has left", "had left"), 3, "Quá khứ hoàn thành xảy ra trước 1 hành động quá khứ khác."));
            list.add(new ExamQuestion("I suggest ______ a taxi to the airport.", Arrays.asList("take", "to take", "taking", "taken"), 2, "Suggest + V-ing."));
            list.add(new ExamQuestion("Choose the synonym of 'Generous':", Arrays.asList("Mean", "Kind", "Selfish", "Humble"), 1, "Generous và Kind đều có nghĩa là tốt bụng/hào phóng."));
            list.add(new ExamQuestion("You ______ better see a doctor today.", Arrays.asList("had", "would", "should", "did"), 0, "Had better = nên làm gì."));
            list.add(new ExamQuestion("The heavy rain prevented us ______ going out.", Arrays.asList("to", "from", "at", "with"), 1, "Prevent someone from doing something."));
            list.add(new ExamQuestion("Identify error: She (A) is (B) enough (C) tall to (D) reach the shelf.", Arrays.asList("A", "B", "C", "D"), 1, "Trật tự đúng: tall enough."));
            list.add(new ExamQuestion("I ______ used to getting up early now.", Arrays.asList("get", "am", "was", "did"), 1, "Be used to + Ving: đã quen với việc gì."));
            list.add(new ExamQuestion("Neither my parents nor my sister ______ going.", Arrays.asList("is", "are", "am", "be"), 0, "Neither...nor chia theo chủ ngữ gần nhất (sister)."));
            list.add(new ExamQuestion("The movie was so ______ that I fell asleep.", Arrays.asList("bore", "boring", "bored", "boredom"), 1, "Tính từ đuôi -ing chỉ tính chất của vật."));
            list.add(new ExamQuestion("I'll call you as soon as I ______.", Arrays.asList("arrive", "will arrive", "arrived", "arriving"), 0, "Sau liên từ chỉ thời gian không dùng will."));
            list.add(new ExamQuestion("You don't like coffee, ______ you?", Arrays.asList("do", "don't", "are", "aren't"), 0, "Câu hỏi đuôi ở dạng khẳng định vì vế đầu phủ định."));

        } else {
            // Mức Khó: 20 câu - Cấp độ C1 & Cấu trúc đặc biệt
            list.add(new ExamQuestion("Hardly ______ had I entered the room than the phone rang.", Arrays.asList("when", "than", "before", "after"), 0, "Cấu trúc Hardly... when."));
            list.add(new ExamQuestion("It's high time you ______ to work seriously.", Arrays.asList("get", "got", "getting", "to get"), 1, "It's high time + S + V-ed."));
            list.add(new ExamQuestion("I'd rather you ______ anyone what I said.", Arrays.asList("don't tell", "didn't tell", "not to tell", "not telling"), 1, "Would rather (S2) dùng quá khứ giả định."));
            list.add(new ExamQuestion("Not only ______ the exam, but she also got a scholarship.", Arrays.asList("she passed", "did she pass", "passed she", "she did pass"), 1, "Đảo ngữ với Not only."));
            list.add(new ExamQuestion("Suppose you ______ a million dollars, what would you do?", Arrays.asList("win", "won", "had won", "winning"), 1, "Giả định không có thật ở hiện tại."));
            list.add(new ExamQuestion("Such ______ the weather that we stayed home.", Arrays.asList("is", "was", "were", "be"), 1, "Đảo ngữ với Such... that."));
            list.add(new ExamQuestion("The ______ of the results will be known tomorrow.", Arrays.asList("outcome", "income", "output", "outset"), 0, "Outcome: kết quả chung cuộc."));
            list.add(new ExamQuestion("I'm sorry, but I can't ______ with your behavior anymore.", Arrays.asList("put up", "put down", "put on", "put off"), 0, "Put up with: chịu đựng."));
            list.add(new ExamQuestion("No sooner ______ the sun set than the stars appeared.", Arrays.asList("had", "did", "has", "was"), 0, "Cấu trúc No sooner... than."));
            list.add(new ExamQuestion("Had it not been for your help, I ______.", Arrays.asList("failed", "would fail", "would have failed", "will fail"), 2, "Đảo ngữ câu điều kiện loại 3."));
            list.add(new ExamQuestion("The government ______ many changes recently.", Arrays.asList("made", "makes", "has made", "had made"), 2, "Recently dùng với Hiện tại hoàn thành."));
            list.add(new ExamQuestion("Unless you ______ harder, you won't pass.", Arrays.asList("study", "don't study", "studied", "will study"), 0, "Unless = If not."));
            list.add(new ExamQuestion("The room ______ when I arrived.", Arrays.asList("cleaned", "was cleaning", "was being cleaned", "is cleaning"), 2, "Quá khứ tiếp diễn dạng bị động."));
            list.add(new ExamQuestion("He denied ______ the window.", Arrays.asList("break", "to break", "breaking", "broken"), 2, "Deny + V-ing."));
            list.add(new ExamQuestion("I regret ______ you that your application was rejected.", Arrays.asList("tell", "to tell", "telling", "told"), 1, "Regret to tell: lấy làm tiếc khi phải báo tin."));
            list.add(new ExamQuestion("Only by ______ hard can you succeed.", Arrays.asList("study", "studying", "to study", "studied"), 1, "Sau giới từ by dùng V-ing."));
            list.add(new ExamQuestion("It is essential that he ______ here on time.", Arrays.asList("is", "be", "was", "been"), 1, "Cấu trúc câu giả định (Subjunctive)."));
            list.add(new ExamQuestion("Providing ______ the weather is fine, we'll go.", Arrays.asList("that", "which", "whose", "whom"), 0, "Providing that = miễn là."));
            list.add(new ExamQuestion("The car ______ he bought is very expensive.", Arrays.asList("who", "which", "whom", "whose"), 1, "Mệnh đề quan hệ chỉ vật."));
            list.add(new ExamQuestion("Little ______ did he know about the surprise party.", Arrays.asList("do", "did", "does", "done"), 1, "Đảo ngữ với Little."));
        }
        return list;
    }

    public static int getTimeLimit(String difficulty) {
        if (difficulty.equals("EASY")) return 10;
        if (difficulty.equals("MEDIUM")) return 15;
        return 20;
    }
}