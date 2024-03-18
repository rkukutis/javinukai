import { useState } from "react";
import getAllCategories from "../../services/categories/getAllCategories";
import PaginationSettings from "../PaginationSettings";
import { useQuery } from "@tanstack/react-query";
import CategoryPreview from "./CategoryPreview";

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

  return (
    <div className="w-[60vw] p-3">
      <PaginationSettings
        pagination={paginationSettings}
        setPagination={setPaginationSettings}
        availablePageNumber={data?.totalPages}
        limitObjectName="categories"
        sortFieldOptions={
          <>
            <option value="name">Name</option>
            <option value="maxTotalSubmissions">Max total submissions</option>
            <option value="maxUserSubmissions">Max user submissions</option>
            <option value="type">Type</option>
          </>
        }
        searchByFieldName="name"
        firstPage={data?.first}
        lastPage={data?.last}
      />
      <div className="xl:grid xl:grid-cols-12 bg-white shadow py-2 px-3 place-items-left rounded mb-2">
        <h2 className="col-span-4">Name</h2>
        <p className="col-span-2">Type</p>
        <p className="col-span-2">Max Total Submissions</p>
        <p className="col-span-2">Max User Submissions</p>
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
