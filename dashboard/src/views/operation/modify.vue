<style lang="less">
@import "./operation.less";
</style>
<template>
  <div>
    <Modal title="编辑运维" :value="value" @on-ok="save" @on-visible-change="visibleChange">
      <Form ref="productForm" label-position="top" :rules="productRule" :model="operater">
        <FormItem label="账户" prop="productName">
          <Input v-model="operater.account" :maxlength="32" :minlength="2" />
        </FormItem>
      </Form>
      <div slot="footer">
        <Button @click="cancel">关闭</Button>
        <Button @click="save" type="primary">保存</Button>
      </div>
    </Modal>
  </div>
</template>
<script lang="ts">
import { Component, Vue, Inject, Prop, Watch } from "vue-property-decorator";
import Util from "@/lib/util";
import AbpBase from "@/lib/abpbase";
import PageRequest from "../../store/entities/page-request";
import Product from "@/store/entities/product";
import auth from "@/lib/auth";
@Component
export default class CreateDevice extends AbpBase {
  @Prop({
    type: Boolean,
    default: false
  })
  value: boolean;
  path: string = "";
  get operater() {
    var t = this.$store.state.operater.editOperater;
    return t;
  }
  save() {
    (this.$refs.productForm as any).validate(async (valid: boolean) => {
      if (valid) {
        await this.$store.dispatch({
          type: "operater/modify",
          data: this.operater
        });
        (this.$refs.productForm as any).resetFields();
        this.$emit("save-success");
        this.$emit("input", false);
      }
    });
  }
  created() {}
  cancel() {
    (this.$refs.productForm as any).resetFields();
    this.$emit("input", false);
  }
  visibleChange(value: boolean) {
    if (!value) {
      this.$emit("input", value);
    }
  }

  productRule = {
    productName: [
      {
        required: true,
        message: "商品名必填",
        trigger: "blur"
      }
    ],
    productNum: [
      {
        required: true,
        message: "商品编号必填",
        trigger: "blur"
      }
    ],
    productType: [
      {
        required: true,
        message: "商品类型必填",
        trigger: "blur"
      }
    ]
  };
}
</script>