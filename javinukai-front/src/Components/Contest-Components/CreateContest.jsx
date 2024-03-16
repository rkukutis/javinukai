import { useState } from "react";
import { useForm } from "react-hook-form";
import axios from "axios";
import FormFieldError from "../FormFieldError";
import CreateCategory from "./CreateCategory";
import { useTranslation } from "react-i18next";
import Modal from "../Modal";
import toast from "react-hot-toast";
import CategoryList from "./CategoryList";
import StyledInput from "../StyledInput";
import Button from "../Button";
import deleteIcon from "../../assets/icons/delete_FILL0_wght400_GRAD0_opsz24.svg";
import editIcon from "../../assets/icons/edit_FILL0_wght400_GRAD0_opsz24.svg";
import EditCategoryModal from "./EditCategoryModal";

function CategoryItemReadOnly({
  category,
  onRemoveCategory,
  onUpdateCategory,
}) {
  const [editModalIsOpen, setEditModalIsOpen] = useState(false);

  return (
    <div className="xl:grid xl:grid-cols-12 bg-white p-2 rounded shadow">
      <h2 className="col-span-4 self-center">{category.name}</h2>
      <h2 className="col-span-2 self-center">{category.type}</h2>
      <h2 className="col-span-2 self-center">{category.maxTotalSubmissions}</h2>
      <h2 className="col-span-2 self-center">{category.maxUserSubmissions}</h2>
      <div className="col-span-2 space-x-2 self-center flex justify-end">
        <Button
          type="button"
          onClick={() => setEditModalIsOpen(true)}
          extraStyle="bg-yellow-500 hover:bg-yellow-400"
        >
          <img src={editIcon} />
        </Button>
        <Button
          type="button"
          extraStyle="bg-red-500 hover:bg-red-400"
          onClick={() => onRemoveCategory(category)}
        >
          <img src={deleteIcon} />
        </Button>
      </div>
      <Modal isOpen={editModalIsOpen} setIsOpen={setEditModalIsOpen}>
        <EditCategoryModal
          onUpdateCategory={onUpdateCategory}
          category={category}
          onClose={() => setEditModalIsOpen(false)}
        />
      </Modal>
    </div>
  );
}

function CategorySelection({ selectedCategories, setSelectedCategories }) {
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

  return (
    <section className="bg-slate-50 p-3 my-2 rounded">
      <h1 className="text-xl mb-2">Categories</h1>
      {selectedCategories.length == 0 ? (
        <p className="text-center py-4 bg-red-100 rounded">
          No categories added yet!
        </p>
      ) : (
        <>
          <div className="xl:grid-cols-12 xl:grid bg-white p-2 rounded shadow">
            <h2 className="col-span-4">Name</h2>
            <h2 className="col-span-2">Type</h2>
            <h2 className="col-span-2">Max total</h2>
            <h2 className="col-span-2">Max user</h2>
          </div>
          <div className="space-y-2 mt-2">
            {selectedCategories.map((category) => (
              <CategoryItemReadOnly
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
          Add categories from database
        </Button>
        <Button
          type="button"
          onClick={() => setCreateCategoryModalIsOpen(true)}
        >
          Create and add new category
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

export default function CreateContest({ contestDTO }) {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm();

  const [selectedCategories, setSelectedCategories] = useState([]);
  const { t } = useTranslation();

  const onSubmit = async (data) => {
    const startDate = new Date(data.startDate).toISOString();
    const endDate = new Date(data.endDate).toISOString();

    if (isNaN(Date.parse(startDate)) || isNaN(Date.parse(endDate))) {
      toast.error("Invalid date values");
      return;
    }
    console.log(data);
    const contestData = {
      ...data,
      startDate,
      endDate,
      categories: selectedCategories,
    };

    await axios
      .post(`${import.meta.env.VITE_BACKEND}/api/v1/contests`, contestData, {
        withCredentials: true,
      })
      .then(() => toast.success("Contest created successfully"))
      .catch(() => toast.error("Ann error has occured"));
  };

  return (
    <div className="w-3/5 mx-auto mt-10 p-6 bg-white rounded-md shadow-md">
      <h2 className="text-2xl font-semibold mb-4">
        {t("CreateContest.contestTitle")}
      </h2>
      <form onSubmit={handleSubmit(onSubmit)}>
        <div className="mb-4">
          <label
            htmlFor="name"
            className="block text-sm font-medium text-gray-700"
          >
            {t("CreateContest.contestName")}
          </label>
          <input
            type="text"
            id="name"
            {...register("name", { required: "Required" })}
            defaultValue={contestDTO && contestDTO.name}
            className="mt-1 p-2 w-full border-2 border-slate-100 rounded-md focus:outline-none focus:border-blue-500"
          />
          {errors.name && (
            <FormFieldError>{errors.name.message}</FormFieldError>
          )}
        </div>
        <div className="mb-4">
          <label
            htmlFor="description"
            className="block text-sm font-medium text-gray-700"
          >
            {t("CreateContest.description")}
          </label>
          <textarea
            id="description"
            {...register("description", { required: "Required" })}
            defaultValue={contestDTO && contestDTO.description}
            className="mt-1 p-2 w-full border-2 border-slate-100 rounded-md focus:outline-none focus:border-blue-500"
          />
          {errors.description && (
            <FormFieldError>{errors.description.message}</FormFieldError>
          )}
        </div>
        <section className="xl:grid xl:grid-cols-2 xl:grid-rows-2 xl:gap-3">
          <div>
            <label
              htmlFor="startDate"
              className="block text-sm font-medium text-gray-700"
            >
              {t("CreateContest.startDate")}
            </label>
            <input
              type="date"
              id="startDate"
              {...register("startDate", {
                required: "Required",
                min: { value: 1 },
              })}
              className="mt-1 p-2 w-full border-2 border-slate-100 rounded-md focus:outline-none focus:border-blue-500"
            />
            {errors.startDate && (
              <FormFieldError>{errors.startDate.message}</FormFieldError>
            )}
          </div>
          <div>
            <label
              htmlFor="endDate"
              className="block text-sm font-medium text-gray-700"
            >
              {t("CreateContest.endDate")}
            </label>
            <input
              type="date"
              id="endDate"
              {...register("endDate", { required: "Required", min: 1 })}
              className="mt-1 p-2 w-full border-2 border-slate-100 rounded-md focus:outline-none focus:border-blue-500"
            />
            {errors.endDate && (
              <FormFieldError>{errors.endDate.message}</FormFieldError>
            )}
          </div>
          <div className="mb-4">
            <label
              htmlFor="maxTotalSubmissions"
              className="block text-sm font-medium text-gray-700"
            >
              Max total submissions
            </label>
            <input
              min={0}
              placeholder={500}
              type="number"
              id="maxTotalSubmissions"
              {...register("maxTotalSubmissions", {
                required: "Required",
                min: 1,
              })}
              className="mt-1 p-2 w-full border-2 border-slate-100 rounded-md focus:outline-none focus:border-blue-500"
            />
            {errors.maxTotalSubmissions && (
              <FormFieldError>
                {errors.maxTotalSubmissions.message}
              </FormFieldError>
            )}
          </div>
          <div className="mb-4">
            <label
              htmlFor="maxUserSubmissions"
              className="block text-sm font-medium text-gray-700"
            >
              Max submissions per user
            </label>
            <input
              min={0}
              placeholder={50}
              type="number"
              id="maxUserSubmissions"
              {...register("maxUserSubmissions", {
                required: "Required",
                min: 1,
              })}
              className="mt-1 p-2 w-full border-2 border-slate-100 rounded-md focus:outline-none focus:border-blue-500"
            />
            {errors.maxUserSubmissions && (
              <FormFieldError>
                {errors.maxUserSubmissions.message}
              </FormFieldError>
            )}
          </div>
        </section>
        <CategorySelection
          selectedCategories={selectedCategories}
          setSelectedCategories={setSelectedCategories}
        />
        <div className="flex justify-end">
          <StyledInput
            extraStyle="px-2 text-lg font-bold"
            value="Create contest"
            type="submit"
          />
        </div>
      </form>
    </div>
  );
}
