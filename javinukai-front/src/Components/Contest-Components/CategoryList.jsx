import { useState } from "react";
import getAllCategories from "../../services/categories/getAllCategories";
import PaginationSettings from "../PaginationSettings";
import { useQuery } from "@tanstack/react-query";
import CategoryPreview from "./CategoryPreview";
import { useTranslation } from "react-i18next";

function checkIfCategoryAlreadySelected(selectedCategories, category) {
  return selectedCategories.filter((sc) => sc.id == category.id).length == 0;
}

const defaultPagination = {
  page: 0,
  limit: 10,
  sortBy: "name",
  sortDesc: "false",
  searchedField: null,
};

function CategoryList({ onAddCategory, selectedCategories }) {
  const [paginationSettings, setPaginationSettings] =
    useState(defaultPagination);
  const { data, isFetching } = useQuery({
    queryKey: [
      "categoriesToSelect",
      paginationSettings.page,
      paginationSettings.limit,
      paginationSettings.sortBy,
      paginationSettings.sortDesc,
      paginationSettings.searchedField,
    ],
    queryFn: () =>
      getAllCategories(
        paginationSettings.page,
        paginationSettings.limit,
        paginationSettings.sortBy,
        paginationSettings.sortDesc,
        paginationSettings.searchedField
      ),
  });

  console.log(data);
  const { t } = useTranslation();
  return (
    <div className="w-[60vw] p-3">
      <PaginationSettings
        pagination={paginationSettings}
        setPagination={setPaginationSettings}
        availablePageNumber={data?.totalPages}
        limitObjectName={t("CategoryList.categoryTitle")}
        sortFieldOptions={
          <>
            <option value="name">{t("CategoryList.categoriesNameCapital")}</option>
            <option value="maxTotalSubmissions">{t("CategoryList.categoriesMaxTotalSubmissions")}</option>
            <option value="maxUserSubmissions">{t("CategoryList.categoriesMaxUserSubmissions")}</option>
            <option value="type">{t("CategoryList.categoriesType")}</option>
          </>
        }
        searchByFieldName={t("CategoryList.categoriesName")}
        firstPage={data?.first}
        lastPage={data?.last}
      />
      <div className="xl:grid xl:grid-cols-12 bg-white shadow py-2 px-3 place-items-left rounded mb-2">
        <h2 className="col-span-4">{t("CategoryList.categoryName")}</h2>
        <p className="col-span-2">{t("CategoryList.categoriesType")}</p>
        <p className="col-span-2">{t("CategoryList.categoriesMaxTotalSubmissions")}</p>
        <p className="col-span-2">{t("CategoryList.categoriesMaxUserSubmissions")}</p>
      </div>
      <div className="bg-slate-50 space-y-2 h-fit overflow-auto">
        {data?.content
          .filter((category) =>
            checkIfCategoryAlreadySelected(selectedCategories, category)
          )
          .map((category) => (
            <CategoryPreview
              key={category.id}
              category={category}
              onAddCategory={onAddCategory}
            />
          ))}
      </div>
    </div>
  );
}

export default CategoryList;
