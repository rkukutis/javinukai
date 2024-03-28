import { useState } from "react";
import CreateCategory from "./CreateCategory";
import Modal from "../Modal";
import CategoryList from "./CategoryList";
import Button from "../Button";
import { SelectedCategoryItem } from "./SelectedCategoryItem";
import { useTranslation } from "react-i18next";

export function CategorySelection({
  selectedCategories,
  setSelectedCategories,
}) {
  const [addCategoryModalOpen, setAddCategoryModalOpen] = useState(false);
  const [createCategoryModalIsOpen, setCreateCategoryModalIsOpen] =
    useState(false);

  function handleAddSelectedCategory(selectedCategory) {
    if (selectedCategories.includes(selectedCategory)) return;
    setSelectedCategories([...selectedCategories, selectedCategory]);
  }

  function handleCategoryUpdate(updatedCategory) {
    const filtered = selectedCategories.filter(
      (c) => c.id != updatedCategory.id
    );
    setSelectedCategories([...filtered, updatedCategory]);
  }

  function handleRemoveSelectedCategory(category) {
    const filtered = selectedCategories.filter(
      (selectedCategory) => selectedCategory.id != category.id
    );
    setSelectedCategories(filtered);
  }
  const { t } = useTranslation();
  return (
    <section className="bg-slate-50 p-3 my-2 rounded">
      <h1 className="text-xl mb-2">{t("CategorySelection.categories")}</h1>
      {selectedCategories.length == 0 ? (
        <p className="text-center py-4 bg-red-100 rounded">
          {t("CategorySelection.noCategoriesAdded")}
        </p>
      ) : (
        <>
          <div className="xl:grid-cols-12 xl:grid bg-white p-2 rounded shadow">
            <h2 className="col-span-4">{t("CategorySelection.categoryName")}</h2>
            <h2 className="col-span-2">{t("CategorySelection.categoryType")}</h2>
            <h2 className="col-span-2">{t("CategorySelection.categoryMaxSubmissions")}</h2>
            <h2 className="col-span-2">{t("CategorySelection.categoryMaxUserSubmissions")}</h2>
          </div>
          <div className="space-y-2 mt-2">
            {selectedCategories.map((category) => (
              <SelectedCategoryItem
                key={category.id}
                category={category}
                onUpdateCategory={handleCategoryUpdate}
                onRemoveCategory={handleRemoveSelectedCategory}
              />
            ))}
          </div>
        </>
      )}
      <div className="mt-2 xl:grid-cols-2 xl:grid xl:gap-4">
        <Button type="button" onClick={() => setAddCategoryModalOpen(true)}>
        {t("CategorySelection.addCategoriesFromDataBase")}
        </Button>
        <Button
          type="button"
          onClick={() => setCreateCategoryModalIsOpen(true)}
        >
          {t("CategorySelection.CreateNewCategory")}
        </Button>
      </div>
      <Modal
        isOpen={createCategoryModalIsOpen}
        setIsOpen={setCreateCategoryModalIsOpen}
      >
        <CreateCategory
          closeModal={() => setCreateCategoryModalIsOpen(false)}
          onCreateCategory={handleAddSelectedCategory}
        />
      </Modal>
      <Modal
        backroundColor="bg-slate-50"
        isOpen={addCategoryModalOpen}
        setIsOpen={setAddCategoryModalOpen}
      >
        <CategoryList
          onAddCategory={handleAddSelectedCategory}
          selectedCategories={selectedCategories}
        />
      </Modal>
    </section>
  );
}
