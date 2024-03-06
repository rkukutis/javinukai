import React from "react";
import axios from "axios";
import { useForm } from "react-hook-form";

function EditCategoryModal({ category, onClose }) {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm();

  const onSubmit = async (data) => {
    try {
      const response = await axios.put(
        `${import.meta.env.VITE_BACKEND}/api/v1/categories/${category.id}`,
        data,
        { withCredentials: true }
      );
      console.log("Category updated successfully:", response.data);
      onClose();
    } catch (error) {
      console.error("Error updating category:", error);
    }
  };

  if (!category) {
    return null;
  }

  return (
    <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50">
      <div className="bg-white p-6 rounded-md shadow-md">
        <h2>Edit Category</h2>
        <form onSubmit={handleSubmit(onSubmit)}>
          <div className="mb-4">
            <label htmlFor="categoryName" className="block text-gray-700">
              Category Name:
            </label>
            <input
              type="text"
              id="categoryName"
              defaultValue={category.categoryName}
              {...register("categoryName", { required: true })}
              className="mt-1 p-2 w-full border-gray-300 rounded-md focus:outline-none focus:border-blue-500"
            />
            {errors.categoryName && (
              <p className="text-red-500">This field is required</p>
            )}
          </div>
          <div className="mb-4">
            <label htmlFor="description" className="block text-gray-700">
              Description:
            </label>
            <textarea
              id="description"
              defaultValue={category.description}
              {...register("description")}
              className="mt-1 p-2 w-full border-gray-300 rounded-md focus:outline-none focus:border-blue-500"
            />
          </div>
          <div className="mb-4">
            <label htmlFor="totalSubmissions" className="block text-gray-700">
              Total Submissions:
            </label>
            <input
              type="number"
              id="totalSubmissions"
              defaultValue={category.totalSubmissions}
              {...register("totalSubmissions")}
              className="mt-1 p-2 w-full border-gray-300 rounded-md focus:outline-none focus:border-blue-500"
            />
          </div>
          <div className="mb-4">
            <label htmlFor="type" className="block text-gray-700">
              Type:
            </label>
            <select
              id="type"
              {...register("type", { required: true })}
              className={`mt-1 p-2 w-full border-gray-300 rounded-md focus:outline-none focus:border-blue-500 ${
                errors.type ? "border-red-500" : ""
              }`}
            >
              <option value="">Select Type</option>
              <option value="SINGLE">Single</option>
              <option value="COLLECTION">Collection</option>
            </select>
            {errors.type && (
              <p className="text-red-500">Please select a type</p>
            )}
          </div>
          <button
            type="submit"
            className="bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600"
          >
            Update Category
          </button>
        </form>
        <button
          onClick={onClose}
          className="bg-gray-500 text-white px-4 py-2 rounded-md hover:bg-gray-600 ml-2"
        >
          Cancel
        </button>
      </div>
    </div>
  );
}

export default EditCategoryModal;
