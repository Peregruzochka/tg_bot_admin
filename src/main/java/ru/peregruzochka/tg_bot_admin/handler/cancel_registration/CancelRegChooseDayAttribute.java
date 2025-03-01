package ru.peregruzochka.tg_bot_admin.handler.cancel_registration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.peregruzochka.tg_bot_admin.handler.BaseAttribute;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "attr.cancel-reg-choose-day")
public class CancelRegChooseDayAttribute extends BaseAttribute {
    private List<String> weekDaysNames;

    private String navigateNextButton;
    private String navigateBackButton;

    private String navigateCallback;
    private String emptyCallback;
    private String chooseDayCallback;

    private String errorText;

    public InlineKeyboardMarkup generateChooseDayMarkup(int monthOffset) {
        List<List<InlineKeyboardButton>> newMarkup = new ArrayList<>();
        newMarkup.add(getWeekNameLine());
        newMarkup.addAll(getMonthDays(monthOffset));
        newMarkup.add(getNavigateLine(monthOffset));
        return super.generateMarkup(newMarkup);
    }

    private List<InlineKeyboardButton> getWeekNameLine() {
        return weekDaysNames.stream()
                .map(day -> createButton(day, emptyCallback))
                .toList();
    }

    private List<InlineKeyboardButton> getNavigateLine(int monthOffset) {
        LocalDate today = LocalDate.now();
        today = today.plusMonths(monthOffset);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy", new Locale("ru"));
        String formattedDate = today.format(formatter);

        InlineKeyboardButton nextButton = createButton(navigateNextButton, navigateCallback + (monthOffset + 1));
        InlineKeyboardButton backButton = createButton(navigateBackButton, navigateCallback + (monthOffset - 1));
        InlineKeyboardButton navigateButton = createButton(formattedDate, emptyCallback);

        return new ArrayList<>() {{
            add(backButton);
            add(navigateButton);
            add(nextButton);
        }};
    }

    private List<List<InlineKeyboardButton>> getMonthDays(int monthOffset) {
        YearMonth yearMonth = YearMonth.now().plusMonths(monthOffset);
        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstDayOfMonth = yearMonth.atDay(1);
        int startDayOfWeek = firstDayOfMonth.getDayOfWeek().getValue();

        List<List<InlineKeyboardButton>> calendar = new ArrayList<>();

        int day = 1;
        for (int i = 0; i < 6; i++) {
            List<InlineKeyboardButton> weekLine = new ArrayList<>();
            for (int j = 0; j < 7; j++) {
                if (i == 0 && j < startDayOfWeek - 1) {
                    weekLine.add(createButton(" ", emptyCallback));
                } else if (day > daysInMonth) {
                    weekLine.add(createButton(" ", emptyCallback));
                } else {
                    weekLine.add(createButton(String.valueOf(day), chooseDayCallback + createDate(yearMonth, day)));
                    day++;
                }
            }
            calendar.add(weekLine);
        }
        return calendar;
    }

    private String createDate(YearMonth yearMonth, int day) {
        LocalDate date = yearMonth.atDay(day);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
    }
}
