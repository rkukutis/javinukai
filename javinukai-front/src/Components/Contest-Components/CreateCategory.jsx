import React, { useState } from "react";
import { useForm } from "react-hook-form";
import axios from "axios";
import FormFieldError from "../FormFieldError";
import { useTranslation } from "react-i18next";
function CreateCategory() {
  const { t } = useTranslation();
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
    setError,
    clearErrors,
  } = useForm();
  const [showErrorMessage, setShowErrorMessage] = useState(false);

  const onSubmit = async (data) => {
    try {
      const response = await axios.post(
        `${import.meta.env.VITE_BACKEND}/api/v1/categories`,
        data,
        { withCredentials: true }
      );
      console.log("Category created successfully:", response.data);
      setShowErrorMessage(false);
    } catch (error) {
      console.error("Error creating category:", error);
      setShowErrorMessage(true);
      setError("submit", {
        type: "manual",
        message: "Error creating category",
      });
    }
  };

  return (
    <div className="max-w-md mx-auto mt-10 p-6 bg-white rounded-md shadow-md">
      <h2 className="text-2xl font-semibold mb-4">{t('CreateCategory.categoryScreen')}</h2>
      <div>
        <div className="mb-4">
          <label htmlFor="name" className="block text-gray-700">
          {t('CreateCategory.categoryName')}
          </label>
          <input
            type="text"
            id="name"
            {...register("name", { required: "Required" })}
            className={`mt-1 p-2 w-full border-gray-300 rounded-md focus:outline-none focus:border-blue-500 ${
              errors.categoryName ? "border-red-500" : ""
            }`}
          />
          {errors.categoryName && (
            <FormFieldError>{errors.name.message}</FormFieldError>
          )}
        </div>
        <div className="mb-4">
          <label htmlFor="description" className="block text-gray-700">
          {t('CreateCategory.categoryDescription')}
          </label>
          <textarea
            id="description"
            {...register("description")}
            className={`mt-1 p-2 w-full border-gray-300 rounded-md focus:outline-none focus:border-blue-500 `}
          />
          {errors.description && (
            <FormFieldError>{errors.description.message}</FormFieldError>
          )}
        </div>
        <div className="mb-4">
          <label htmlFor="totalSubmissions" className="block text-gray-700">
          {t('CreateCategory.categoryTotalSubmissions')}
          </label>
          <input
            type="number"
            id="totalSubmissions"
            {...register("totalSubmissions", { required: "Required", min: 1 })}
            className={`mt-1 p-2 w-full border-gray-300 rounded-md focus:outline-none focus:border-blue-500`}
          />
          {errors.totalSubmissions && (
            <FormFieldError>{errors.totalSubmissions.message}</FormFieldError>
          )}
        </div>
        <div className="mb-4">
          <label htmlFor="type" className="block text-gray-700">
          {t('CreateContest.category')}
          </label>
          <select
            id="type"
            {...register("type", { required: "Pleae Select a type" })}
            className={`mt-1 p-2 w-full border-gray-300 rounded-md focus:outline-none focus:border-blue-500 ${
              errors.type ? "border-red-500" : ""
            }`}
          >
            <option value="">{t('CreateCategory.categorySelectType')}</option>
            <option value="SINGLE">{t('categoryTypes.SINGLE')}</option>
            <option value="COLLECTION">{t('categoryTypes.COLLECTION')}</option>
          </select>

          {errors.type && (
            <FormFieldError>{errors.type.message} </FormFieldError>
          )}
        </div>
        <div className="mb-4">
          <button
            onClick={handleSubmit(onSubmit)}
            disabled={isSubmitting}
            className="bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600 relative"
          >
            {isSubmitting && (
              <span className="absolute inset-0 flex items-center justify-center">
                <div className="loader"></div>
              </span>
            )}
            {t('CreateCategory.categoryCreateButton')}
          </button>
          {showErrorMessage && (
            <p className="text-red-500">Error creating category</p>
          )}
        </div>
      </div>
    </div>
  );
}

export default CreateCategory;
