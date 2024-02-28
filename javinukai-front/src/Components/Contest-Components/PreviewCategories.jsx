import React, { useState, useEffect } from "react";
import axios from "axios";
import EditCategoryModal from "./EditCategoryModal";

function PreviewCategories() {
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [selectedCategory, setSelectedCategory] = useState(null);
  const [showModal, setShowModal] = useState(false);

  useEffect(() => {
    async function fetchData() {
      try {
        const response = await axios.get(
          `${
            import.meta.env.VITE_BACKEND
          }/api/v1/categories?pageNumber=1&pageSize=5&sortBy=categoryName&sortDesc=false`
        );
        setCategories(response.data.content);
        setLoading(false);
      } catch (error) {
        setError(error.message);
        setLoading(false);
      }
    }

    fetchData();
  }, []);

  const handleEditCategory = (categoryId) => {
    const categoryToEdit = categories.find(
      (category) => category.id === categoryId
    );
    setSelectedCategory(categoryToEdit);
    setShowModal(true);
  };

  const handleCloseModal = () => {
    setShowModal(false);
    setSelectedCategory(null);
  };

  if (loading) {
    return <div className="text-center mt-4">Loading...</div>;
  }

  if (error) {
    return <div className="text-center mt-4">Error: {error}</div>;
  }

  return (
    <div className="p-4">
      <h2 className="text-2xl font-semibold mb-4">Categories</h2>
      <ul>
        {categories.map((category) => (
          <li key={category.id} className="mb-4 border-b pb-4">
            <div>
              <strong>Name:</strong> {category.categoryName}
            </div>
            <div>
              <strong>Description:</strong> {category.description}
            </div>
            <div>
              <strong>Total Submissions:</strong> {category.totalSubmissions}
            </div>
            <div>
              <strong>Type:</strong> {category.type}
            </div>
            <div>
              <strong>Created At:</strong>{" "}
              {new Date(category.createdAt).toLocaleString()}
            </div>
            <div>
              <strong>Modified At:</strong>{" "}
              {new Date(category.modifiedAt).toLocaleString()}
            </div>
            <button
              onClick={() => handleEditCategory(category.id)}
              className="mt-2 bg-blue-500 hover:bg-blue-600 text-white font-semibold py-2 px-4 rounded"
            >
              Edit
            </button>
          </li>
        ))}
      </ul>

      {showModal && (
        <div className="fixed top-0 left-0 w-full h-full flex items-center justify-center bg-black bg-opacity-50">
          <div className="bg-white p-4 rounded shadow-lg">
            <EditCategoryModal
              category={selectedCategory}
              onClose={handleCloseModal}
            />
          </div>
        </div>
      )}
    </div>
  );
}

export default PreviewCategories;
