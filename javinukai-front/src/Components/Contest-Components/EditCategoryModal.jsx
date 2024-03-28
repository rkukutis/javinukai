import axios from "axios";
import { useForm } from "react-hook-form";
import toast from "react-hot-toast";
import StyledInput from "../StyledInput";
import { useTranslation } from "react-i18next";
function EditCategoryModal({ category, onClose, onUpdateCategory }) {
  const {
    
    register,
    handleSubmit,
    formState: { errors },
  } = useForm({ defaultValues: category });

  const onSubmit = async (data) => {
    await axios
      .put(
        `${import.meta.env.VITE_BACKEND}/api/v1/categories/${category.id}`,
        data,
        { withCredentials: true }
      )
      .then((res) => {
        toast.success(t("EditCategoryModal.categoryUpdatedSuccess"));
        onUpdateCategory(res.data);
        onClose();
      })
      .catch(() => toast.error(t("EditCategoryModal.categoryUpdatedError")));
  };

  const { t } = useTranslation();

  if (!category) {
    return null;
  }
  return (
    <div className="p-6 rounded w-[50vw] h-fit">
      <h2>{t("EditCategoryModal.editCategory")}</h2>
      <form id="category-edit-form" onSubmit={handleSubmit(onSubmit)}>
        <div className="mb-4">
          <label htmlFor="name" className="block text-gray-700">
          {t("EditCategoryModal.categoryName")}
          </label>
          <input
            type="text"
            id="name"
            defaultValue={category.categoryName}
            {...register("name", { required: true })}
            className="mt-1 p-2 w-full border-2 border-slate-100 rounded-md focus:outline-none focus:border-blue-500"
          />
          {errors.categoryName && (
            <p className="text-red-500">{t("EditCategoryModal.required")}</p>
          )}
        </div>
        <div className="mb-8 h-[40vh]">
          <label htmlFor="description" className="block text-gray-700">
          {t("EditCategoryModal.categoryDescription")}
          </label>
          <textarea
            id="description"
            defaultValue={category.description}
            {...register("description")}
            className="mt-1 p-2 w-full h-full border-2 border-slate-100 rounded-md focus:outline-none focus:border-blue-500"
          />
        </div>
        <section className="xl:grid xl:grid-cols-3 xl:space-x-2">
          <div className="mb-4">
            <label
              htmlFor="maxTotalSubmissions"
              className="block text-gray-700"
            >
              {t("EditCategoryModal.categoryMaxSubmissions")}
            </label>
            <input
              type="number"
              id="maxTotalSubmissions"
              defaultValue={category.totalSubmissions}
              {...register("maxTotalSubmissions")}
              className="mt-1 p-2 w-full border-2 border-slate-100 rounded-md focus:outline-none focus:border-blue-500"
            />
          </div>
          <div className="mb-4">
            <label htmlFor="maxUserSubmissions" className="block text-gray-700">
            {t("EditCategoryModal.categoryMaxUserSubmissions")}
            </label>
            <input
              type="number"
              id="maxUserSubmissions"
              defaultValue={category.totalSubmissions}
              {...register("maxUserSubmissions")}
              className="mt-1 p-2 w-full border-2 border-slate-100 rounded-md focus:outline-none focus:border-blue-500"
            />
          </div>
          <div className="mb-4">
            <label htmlFor="type" className="block text-gray-700">
            {t("EditCategoryModal.categoryType")}
            </label>
            <select
              id="type"
              {...register("type", { required: true })}
              className={`mt-1 p-2 w-full border-gray-300 rounded-md focus:outline-none focus:border-blue-500 ${
                errors.type ? "border-red-500" : ""
              }`}
            >
              <option value="">{t("CreateCategory.categorySelectType")}</option>
              <option value="SINGLE">{t("categoryTypes.SINGLE")}</option>
              <option value="COLLECTION">{t("categoryTypes.COLLECTION")}</option>
            </select>
            {errors.type && (
              <p className="text-red-500">{t("EditCategoryModal.categoryType")}</p>
            )}
          </div>
        </section>
        <StyledInput
          form="category-edit-form"
          extraStyle="w-full"
          type="submit"
          value={t("EditCategoryModal.updateCategory")}
        />
      </form>
    </div>
  );
}

export default EditCategoryModal;
