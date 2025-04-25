<template>
  <div
    class="modal fade"
    id="addCategoryModal"
    tabindex="-1"
    aria-labelledby="addCategoryModalLabel"
    aria-hidden="true"
  >
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="addCategoryModalLabel">Thêm danh mục mới</h5>
          <button
            type="button"
            class="btn-close"
            data-bs-dismiss="modal"
            aria-label="Close"
          ></button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="$emit('add-category', newCategory)">
            <div class="mb-3">
              <label for="categoryName" class="form-label">Tên danh mục</label>
              <input
                type="text"
                class="form-control"
                id="categoryName"
                v-model="newCategory.name"
                required
              />
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
              <button type="submit" class="btn btn-primary">Thêm danh mục</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { Modal } from 'bootstrap'

export default {
  name: 'AddCategoryModal',
  data() {
    return {
      newCategory: {
        name: '',
      },
      modal: null,
    }
  },
  methods: {
    show() {
      this.modal.show()
    },
    hide() {
      this.modal.hide()
    },
  },
  mounted() {
    this.modal = new Modal(document.getElementById('addCategoryModal'))
  },
}
</script> 