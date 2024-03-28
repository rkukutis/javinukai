import { useState } from "react";
import { useForm } from "react-hook-form";
import axios from "axios";
import FormFieldError from "../FormFieldError";
import { useTranslation } from "react-i18next";
import toast from "react-hot-toast";

function CreateCategory({ onCreateCategory, closeModal }) {
  const { t } = useTranslation();
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm();

  const onSubmit = async (data) => {
    await axios
      .post(`${import.meta.env.VITE_BACKEND}/api/v1/categories`, data, {
        withCredentials: true,
      })
      .then((res) => {
        onCreateCategory(res.data);
        toast.success(t("CreateCategory.categoryCreateSuccess"));
        closeModal();
      })
      .catch(() => toast.error(t("CreateCategory.categoryCreateError")));
  };

  return (
    <div className="w-[50vw] p-6">
      <div>
        <div className="mb-4">
          <label htmlFor="name" className="block text-gray-700">
            {t("CreateCategory.categoryName")}
          </label>
          <input
            type="text"
            id="name"
            {...register("name", { required: t('CreateCategory.required') })}
            className={`mt-1 p-2 w-full border-2 border-slate-100 rounded-md focus:outline-none focus:border-blue-500 ${
              errors.name ? "border-red-500" : ""
            }`}
          />
          {errors.name && (
            <FormFieldError>{errors.name.message}</FormFieldError>
          )}
        </div>
        <div className="mb-4">
          <label htmlFor="description" className="block text-gray-700">
            {t("CreateCategory.categoryDescription")}
          </label>
          <textarea
            id="description"
            {...register("description", { required: t('CreateCategory.required') })}
            className={`mt-1 p-2 w-full border-2 border-slate-100 rounded-md focus:outline-none focus:border-blue-500 `}
          />
          {errors.description && (
            <FormFieldError>{errors.description.message}</FormFieldError>
          )}
        </div>
        <section className="xl:grid xl:grid-cols-3 xl:gap-3">
          <div className="mb-4">
            <label
              htmlFor="maxTotalSubmissions"
              className="block text-gray-700"
            >
              {t("CreateCategory.categoryTotalSubmissions")}
            </label>
            <input
              min={0}
              type="number"
              id="maxTotalSubmissions"
              {...register("maxTotalSubmissions", {
                required: t('CreateCategory.required'),
                min: 1,
              })}
              className={`mt-1 p-2 w-full border-2 border-slate-100 rounded-md focus:outline-none focus:border-blue-500`}
            />
            {errors.maxTotalSubmissions && (
              <FormFieldError>
                {errors.maxTotalSubmissions.message}
              </FormFieldError>
            )}
          </div>
          <div className="mb-4">
            <label htmlFor="maxUserSubmissions" className="block text-gray-700">
            {t("CategorySelection.categoryMaxUserSubmissions")}
            </label>
            <input
              min={0}
              type="number"
              id="maxUserSubmissions"
              {...register("maxUserSubmissions", {
                required: t('CreateCategory.required'),
                min: 1,
              })}
              className={`mt-1 p-2 w-full border-2 border-slate-100 rounded-md focus:outline-none focus:border-blue-500`}
            />
            {errors.maxUserSubmissions && (
              <FormFieldError>
                {errors.maxUserSubmissions.message}
              </FormFieldError>
            )}
          </div>
          <div className="mb-4">
            <label htmlFor="type" className="block text-gray-700">
            {t("CreateCategory.categoryType")}
            </label>
            <select
              id="type"
              {...register("type", { required: t('CreateCategory.required') })}
              className={`mt-1 p-2 w-full border-2 border-slate-100 rounded-md focus:outline-none focus:border-blue-500 ${
                errors.type ? "border-red-500" : ""
              }`}
            >
              <option value="">{t("CreateCategory.categorySelectType")}</option>
              <option value="SINGLE">{t("categoryTypes.SINGLE")}</option>
              <option value="COLLECTION">{t("categoryTypes.COLLECTION")}</option>
            </select>

            {errors.type && (
              <FormFieldError>{errors.type.message} </FormFieldError>
            )}
          </div>
          <div className="mb-4"></div>
        </section>
        <button
          onClick={handleSubmit(onSubmit)}
          className="bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600 relative"
        >
          {isSubmitting && (
            <span className="absolute inset-0 flex items-center justify-center">
              <div className="loader"></div>
            </span>
          )}
          {t("CreateCategory.categoryCreateButton")}
        </button>
      </div>
    </div>
  );
}

export default CreateCategory;
