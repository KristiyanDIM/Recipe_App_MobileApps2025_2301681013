# Cook Book – Mobile Recipe App

**Cook Book** е мобилно приложение за Android, което позволява на потребителите да съхраняват, редактират и организират своите рецепти на едно място.  
Приложението е разработено с фокус върху **чиста архитектура**, **сигурност** и **модерен потребителски интерфейс**.

---

## Идея

Дигитално хранилище за рецепти – добавяне, преглед, редактиране, изтриване и снимки. Заменя тетрадките и листчетата с удобно мобилно приложение.

---

## Как работи

Рецептите се записват локално в **Room база данни** и не се губят след рестарт. Екранът се обновява автоматично чрез **LiveData + Flow**. Камерата използва **Intent + FileProvider** за сигурност. Поддържа **Light/Dark** режим според телефона. Основни функционалности: Добавяне на рецепта, Преглед на всички рецепти, Редактиране на рецепта, Изтриване на рецепта.

---

## Архитектура на приложението

Приложението следва **MVVM (Model-View-ViewModel)** архитектурен модел, допълнен с **Repository** слой:

- **View** – HomeFragment, AddRecipeFragment, DetailRecipeFragment
- **ViewModel** – RecipeViewModel (LiveData, бизнес логика)
- **Repository** – RecipeRepository (единствен източник)
- **Model** – Recipe, RecipeDao, AppDatabase

---

## Потребителски поток

1. Стартиране → списък с рецепти
2. Натиска **+** → попълва форма (заглавие, съставки, инструкции, снимка) → **Запази**
3. Клик върху рецепта → преглед на детайли
4. **Редактирай** → промяна → **Запази** / **Отхвърли**
5. **Изтрий** → диалог за потвърждение
   
---

## Скрийншотове

<img width="484" height="951" alt="add_recipe_dark" src="https://github.com/user-attachments/assets/641da12d-00ee-446f-b704-d83ebd723e3a" />
<img width="583" height="1293" alt="home_light" src="https://github.com/user-attachments/assets/eb73d91a-911e-4287-b0cf-bf07681e2550" />
<img width="580" height="1288" alt="home_dark" src="https://github.com/user-attachments/assets/0c27d8bb-2b87-45f4-aba8-801f0065a106" />
<img width="476" height="1148" alt="detail_light" src="https://github.com/user-attachments/assets/6ef0c70e-2583-4c97-838f-3d1f7e4073a0" />
<img width="481" height="1154" alt="detail_dark" src="https://github.com/user-attachments/assets/be4a649c-a61e-4251-a8ca-2310f5adb8b4" />
<img width="476" height="957" alt="add_recipe_light" src="https://github.com/user-attachments/assets/8e92b120-836c-4b06-a713-f8ac88192f12" />

---

## APK файл

APK файлът е достъпен в папка [`/apk`](./apk) на това репозитори
---
