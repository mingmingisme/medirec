from utils import *
import torch
import numpy as np
import cv2
import pydicom
import matplotlib.pyplot as plt
from skimage import img_as_float32 as img_as_float
import sys
import os


def norm(image):
    return (image - image.min()) / (image.max() - image.min())


def jpg_sino_to_net(inputUrl, outputUrl, model_param):
    input_sino = cv2.imread(inputUrl, 0)
    # input_sino = img_as_float(input_sino)
    input_sino = torch.Tensor(input_sino).unsqueeze(0).unsqueeze(0).to('cuda:0')

    sparse_view_fbp = norm(get_fbp(input_sino, n_angles=128))

    # load model parameters
    net = FBPCONVNet().to('cuda:0')
    net.load_state_dict(torch.load(model_param), strict=True)
    net.eval()
    with torch.no_grad():  # 验证的时候不用计算梯度，取消掉它减少时间和空间的损耗
        output = net(sparse_view_fbp)
    output = output.to('cpu').squeeze(0).squeeze(0).numpy()
    plt.imsave(outputUrl, output, cmap='gray')


if __name__ == '__main__':
    print(f'Current path is {os.getcwd()}')
    inputUrl = sys.argv[1]
    outputUrl = sys.argv[2]
    model_param = sys.argv[3]
    print('Converting image from {} to {}...'.format(inputUrl, outputUrl))
    jpg_sino_to_net(inputUrl, outputUrl, model_param)
    print("Finish conversion")
