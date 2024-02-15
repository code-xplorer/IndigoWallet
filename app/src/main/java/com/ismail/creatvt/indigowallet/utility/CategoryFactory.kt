package com.ismail.creatvt.indigowallet.utility

import com.ismail.creatvt.indigowallet.db.entity.Category
import com.ismail.creatvt.indigowallet.db.entity.MoneyTransaction.Companion.TYPE_EXPENSE
import com.ismail.creatvt.indigowallet.db.entity.MoneyTransaction.Companion.TYPE_INCOME

object CategoryFactory {

    val defaultCategories = arrayListOf(
        Category(
            name = "Health",
            icon = "icon_category_first_aid_kit",
            type = TYPE_EXPENSE,
            color = "color_green"
        ),
        Category(
            name = "Pet",
            icon = "icon_category_dog_square_face",
            type = TYPE_EXPENSE,
            color = "color_light_green"
        ),
        Category(
            name = "Shopping",
            icon = "icon_category_cart",
            type = TYPE_EXPENSE,
            color = "color_light_red"
        ),
        Category(
            name = "Transportation",
            icon = "icon_category_train",
            type = TYPE_EXPENSE,
            color = "color_yellow"
        ),
        Category(
            name = "Investment",
            icon = "icon_category_money_return",
            type = TYPE_EXPENSE,
            color = "color_red"
        ),
        Category(
            name = "Travel",
            icon = "icon_category_travel_bag",
            type = TYPE_EXPENSE,
            color = "color_purple"
        ),
        Category(
            name = "Gifts",
            icon = "icon_category_gift_box",
            type = TYPE_EXPENSE,
            color = "color_light_blue"
        ),
        Category(
            name = "Home expenses",
            icon = "icon_category_home_money",
            type = TYPE_EXPENSE,
            color = "color_dark_blue"
        ),
        Category(
            name = "Food",
            icon = "icon_category_burger",
            type = TYPE_EXPENSE,
            color = "color_orange"
        ),
        Category(
            name = "Fitness",
            icon = "icon_category_dumbell",
            type = TYPE_EXPENSE,
            color = "color_dark_green"
        ),
        Category(
            name = "Education",
            icon = "icon_category_graduation_cap",
            type = TYPE_EXPENSE,
            color = "color_green"
        ),
        Category(
            name = "Entertainment",
            icon = "icon_category_dice",
            type = TYPE_EXPENSE,
            color = "color_light_red"
        ),
        Category(
            name = "Donation",
            icon = "icon_category_fist_giving_money",
            type = TYPE_EXPENSE,
            color = "color_light_green"
        ),
        Category(
            name = "Other",
            icon = "icon_category_squares",
            type = TYPE_EXPENSE,
            color = "color_light_brown"
        ),
        Category(
            name = "Salary",
            icon = "icon_category_money_notes",
            type = TYPE_INCOME,
            color = "color_green"
        ),
        Category(
            name = "Award",
            icon = "icon_category_prize_star",
            type = TYPE_INCOME,
            color = "color_light_blue"
        ),
        Category(
            name = "Bonus",
            icon = "icon_category_money_pouch",
            type = TYPE_INCOME,
            color = "color_light_indigo"
        ),
        Category(
            name = "Lottery",
            icon = "icon_category_lottery",
            type = TYPE_INCOME,
            color = "color_yellow"
        ),
        Category(
            name = "Tips",
            icon = "icon_category_money_bulb",
            type = TYPE_INCOME,
            color = "color_light_red"
        ),
        Category(
            name = "Investment",
            icon = "icon_category_money_return",
            type = TYPE_INCOME,
            color = "color_green"
        ),
        Category(
            name = "Dividend",
            icon = "icon_category_line_graph",
            type = TYPE_INCOME,
            color = "color_blue"
        ),
        Category(
            name = "Other",
            icon = "icon_category_squares",
            type = TYPE_INCOME,
            color = "color_light_brown"
        ),
    )
}